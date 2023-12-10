import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.System.out;
import java.util.*;
import java.io.*;
import java.math.*;

public class Second {
    enum Type {
        Vert,
        Hori,
        L,
        J,
        Seven,
        F,
        Dot,
        S
    }

    enum Direction {
        Up,
        Down,
        Left,
        Right
    }

    static final Map<Character, Type> charToType = new HashMap<>(){{
        put('|', Type.Vert);
        put('-', Type.Hori);
        put('L', Type.L);
        put('J', Type.J);
        put('7', Type.Seven);
        put('F', Type.F);
        put('.', Type.Dot);
        put('S', Type.S);

    }};
    static final Map<Type, int[][]> typeToShape = new HashMap<>(){{
        put(Type.Vert, new int[][] {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        });
        put(Type.Hori, new int[][] {
            {0, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
        });
        put(Type.L, new int[][] {
            {0, 1, 0},
            {0, 1, 1},
            {0, 0, 0}
        });
        put(Type.J, new int[][] {
            {0, 1, 0},
            {1, 1, 0},
            {0, 0, 0}
        });
        put(Type.Seven, new int[][] {
            {0, 0, 0},
            {1, 1, 0},
            {0, 1, 0}
        });
        put(Type.F, new int[][] {
            {0, 0, 0},
            {0, 1, 1},
            {0, 1, 0}
        });
        put(Type.Dot, new int[][] {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        });
        put(Type.S, new int[][] {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        });
    }};

    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("input.txt");
        Scanner sc = new Scanner(in);
        // Scanner sc = new Scanner(System.in);

        List<List<Type>> mapL = new ArrayList<>();

        while (sc.hasNextLine()) {
            List<Type> line = parseLine(sc.nextLine());
            mapL.add(line);
        }
        Type[][] map = convertMapToArr(mapL);
        List<int[]> loop = findLoop(map);
        int[][] enlarged = enlarge(map, loop);
        dfsColor(enlarged);
        int[][] shrinked = shrink(enlarged);
        for (int[] is : shrinked) {
            for (int x: is) {
                System.out.print(x);
            }
            System.out.println();
        }
        long res = Arrays.stream(shrinked).flatMapToInt(l -> Arrays.stream(l)).filter(v -> v == 0).count();
        System.out.println(res);
        
    }

    private static int[][] shrink(int[][] enlarged) {
        int[][] res = new int[(enlarged.length - 1) /  3][(enlarged[0].length - 1) /  3];

        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                if (enlarged[3 * i + 1][3 * j + 1] == 0) {
                    res[i][j] = 0;
                } else {
                    res[i][j] = 1;
                }
            }
        }
        return res;
    }


    private static boolean validCoord(int i, int j, int n, int m) {
        return !(i < 0 || j < 0 || i >= n || j >= m);
    }

    private static void dfsColor(int[][] enlarged) {
        Direction[] directions = {Direction.Up, Direction.Down, Direction.Left, Direction.Right};
        Deque<int[]> stack = new LinkedList<>();
        stack.push(new int[]{0, 0});
        while (!stack.isEmpty()) {
            int[] coords = stack.pop();
            if (!validCoord(coords[0], coords[1], enlarged.length, enlarged[0].length)) {
                continue;
            }
            if (enlarged[coords[0]][coords[1]] != 0) {
                continue;
            }
            enlarged[coords[0]][coords[1]] = 2;
            for (Direction direction : directions) {
                stack.push(getNextTile(coords[0], coords[1], direction));
            }

        }
    }

    private static int[][] enlarge(Type[][] map, List<int[]> loop) {
        int[][] mapI = new int[1 + map.length * 3][1 + map[0].length * 3];
        for (int i = 0; i < mapI.length; i++) {
            for (int j = 0; j < mapI.length; j++) {
                mapI[i][j] = 0;
            }
        }
        for (int[] loopCoord : loop) {
            int ci = loopCoord[0];
            int cj = loopCoord[1];
            for (int i = 3 * ci - 1; i <= 3 * ci + 1; i++) {
                for (int j = 3 * cj - 1; j <= 3 * cj + 1; j++) {
                    mapI[1 + i][1 + j] = typeToShape.get(map[ci][cj])[i - (3 * ci - 1)][j - (3 * cj - 1)];
                }
            }
        }
        return mapI;
    }

    private static List<int[]> findLoop(Type[][] map) {
        // algo: starting from S, go NESW, check if it loops, mark loop 
        int[] sPos = findSPos(map);
        Direction[] directions = {Direction.Up, Direction.Down, Direction.Left, Direction.Right};
        List<int[]> acc = null;
        for (int i = 0; i < directions.length; i++) {
            int coords[] = getNextTile(sPos[0], sPos[1], directions[i]);
            acc = findLoopDirected(map, coords[0], coords[1], directions[i], new ArrayList<>(List.of(coords)));
            if (acc != null) {
                break;
            }
        }
        return acc;
    }


    private static List<int[]> findLoopDirected(Type[][] map, int i, int j, Direction dir, List<int[]> acc) {
        while (true) {
            if (i < 0 || j < 0 || i >= map.length || j >= map[i].length) {
                return null;
            }
            if (map[i][j] == Type.Dot) {
                return null;
            }
            if (map[i][j] == Type.S) {
                return acc;
            } 
            Direction newDir;
            try {
                newDir = getNewDir(map, i, j, dir);
            } catch (IllegalStateException ie) {
                return null;
            }
            newDir = getNewDir(map, i, j, dir);
            int[] next = getNextTile(i, j, newDir);
            i = next[0];
            j = next[1];
            dir = newDir;
            acc.add(next);
        }
    }

    private static int[] getNextTile(int i, int j, Direction newDir) {
        switch(newDir) {
            case Down:
                return new int[] {i + 1, j};
            case Left:
                return new int[] {i, j - 1};
            case Right:
                return new int[] {i, j + 1};
            case Up:
                return new int[] {i - 1, j};
        }
        throw new IllegalStateException(newDir.name());
    }

    private static Direction getNewDir(Type[][] map, int i, int j, Direction dir) {
        switch(map[i][j]) {
        case F:
            if (dir == Direction.Left) {
                return Direction.Down;
            } else if (dir == Direction.Up) {
                return Direction.Right;
            }
        case Hori: // -
            if (dir == Direction.Left) {
                return Direction.Left;
            } else if (dir == Direction.Right) {
                return Direction.Right;
            }
        case J:
            if (dir == Direction.Right) {
                return Direction.Up;
            } else if (dir == Direction.Down) {
                return Direction.Left;
            }
        case L:
            if (dir == Direction.Left) {
                return Direction.Up;
            } else if (dir == Direction.Down) {
                return Direction.Right;
            }
        case Seven: // 7
            if (dir == Direction.Right) {
                return Direction.Down;
            } else if (dir == Direction.Up) {
                return Direction.Left;
            }
        case Vert: // |
            if (dir == Direction.Up) {
                return Direction.Up;
            } else if (dir == Direction.Down) {
                return Direction.Down;
            }
        default:
            throw new IllegalStateException(String.format("%s - %s", map[i][j].name(), dir.name()));
        }
    }

    private static int[] findSPos(Type[][] map) {
        int[] res = new int[2];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == Type.S) {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        throw new IllegalStateException("S not found");
    }

    private static Type[][] convertMapToArr(List<List<Type>> mapL) {
        return mapL.stream().map(l -> l.stream().toArray(Type[]::new)).toArray(Type[][]::new);
    }

    private static List<Type> parseLine(String nextLine) {
        return nextLine.chars().mapToObj(c -> charToType.get((char) c)).toList();
    }
}

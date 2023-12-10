package olymp;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.System.out;
import java.util.*;
import java.io.*;
import java.math.*;

public class First {
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
        int loopLen = findLoopLen(map);
        System.out.println((loopLen + 1) / 2);
    }

    private static int findLoopLen(Type[][] map) {
        // algo: starting from S, go NESW, check if it loops, find length

        int[] sPos = findSPos(map);
        int sx = sPos[0];
        int sy = sPos[1];

        int len = -1;
        len = max(len, findLoopLenDirected(map, sx - 1, sy, Direction.Up, 0));
        // System.out.println(len);
        // System.out.println();
        len = max(len, findLoopLenDirected(map, sx + 1, sy, Direction.Down, 0));
        // System.out.println(len);
        // System.out.println();
        len = max(len, findLoopLenDirected(map, sx, sy - 1, Direction.Left, 0));
        // System.out.println(len);
        // System.out.println();
        len = max(len, findLoopLenDirected(map, sx, sy + 1, Direction.Right, 0));
        return len;
    }


    private static int findLoopLenDirected(Type[][] map, int i, int j, Direction dir, int acc) {
        while (true) {
            if (i < 0 || j < 0 || i > map.length || j > map[i].length) {
                return -1;
            }
            if (map[i][j] == Type.Dot) {
                return -1;
            }
            if (map[i][j] == Type.S) {
                return acc;
            } 
            Direction newDir;
            try {
                newDir = getNewDir(map, i, j, dir);
            } catch (IllegalStateException ie) {
                System.err.println(ie.getMessage());
                return - 1;
            }
            // System.out.printf("(%d %d)[%s], %s\n", i, j, map[i][j].name(), dir.name());
            newDir = getNewDir(map, i, j, dir);
            int[] next = getNextTile(i, j, newDir);
            // tailrec
            i = next[0];
            j = next[1];
            dir = newDir;
            acc += 1;
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

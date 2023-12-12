import sys
import subprocess
import difflib
from enum import Enum

TEST_SEPARATOR = '[TEST]\n'
EXPECTED_SEPARATOR = '[EXPECTED]\n'

class TestMode(Enum):
    TEST = 1
    EXP = 2

class bcolors:
    OKGREEN = '\033[92m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'

def runTests(sol: str, test: str):
    with open(test, 'r') as testFile:
        lines = testFile.readlines()
        lines = lines[1:] # skip first TEST_SEPARATOR
        testContent = ""
        expectedResult = ""
        mode = TestMode.TEST
        count = 1
        for line in lines:
            if line == EXPECTED_SEPARATOR:
                mode = TestMode.EXP
                continue
            if line == TEST_SEPARATOR:
                print(f'{bcolors.BOLD}[ TEST {count} ]{bcolors.ENDC}')
                res, diff = runSingleTest(sol, testContent, expectedResult)
                if diff:
                    printError(count, testContent, expectedResult, res)
                else:
                    print(f'{bcolors.OKGREEN}OK!{bcolors.ENDC}')
                count += 1
                testContent = ""
                expectedResult = ""
                mode = TestMode.TEST
                continue
            if mode == TestMode.TEST:
                testContent += line
                continue
            if mode == TestMode.EXP:
                expectedResult += line
                continue

        # process last test case
        if mode == TestMode.EXP:
            print(f'{bcolors.BOLD}[ TEST {count} ]{bcolors.ENDC}')
            res, diff = runSingleTest(sol, testContent, expectedResult)
            if diff:
                printError(count, testContent, expectedResult, res)

def printError(count: int, testContent: str, expectedResult: str, res: str):
    print(f'{bcolors.FAIL}ERROR{bcolors.ENDC}\n' +
          f'--- INPUT\n{testContent}' + 
          f'--- EXPECTED\n{expectedResult}' +
          f'--- OUTPUT\n{res}')
 

def runSingleTest(sol: str, testContent: str, expectedResult: str):
    result = subprocess.run(["java", sol], \
            input=testContent.encode('utf-8'), stdout=subprocess.PIPE)
    outContent = result.stdout.decode('utf-8')
    diff = difflib.unified_diff(outContent, expectedResult)
    diff = ''.join(diff)

    return outContent, diff

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: ./judge.sh <problem>")
        sys.exit(1)
    problem = sys.argv[1]
    runTests(problem + ".java", "tests/" + problem)

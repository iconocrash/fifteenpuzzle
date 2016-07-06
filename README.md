# 15-Puzzle Solver

## To compile
Run `javac *.java` in the "src/fifteenpuzzle" directory

## To run
Run `java fifteenpuzzle.FifteenPuzzleMain` from inside the "src/"" directory (not the "src/fifteenpuzzle" directory) after compiling.

## Information

This program can try to find a solution for 15-puzzle problems using a suite of search algorithms, including IDLS and A* with two different heuristics. A* heuristic 1 uses the sum of the number tiles that are out of place when compared to the goal board as the h() value. A* heuristic 2 uses the sum of the "Manhattan distances" from each tile to their goal location as the h() value.

After running the program, the user will be given a selection menu. From here the user can create a board (either by user input for the entire puzzle or by copying another board), load a board, or delete a board.

The user can test running a search algorithm on the currently loaded board by going to (5) and choosing the desired algorithm. 
Option (6) allows the user to run a statistics test comparing the IDLS, A*h1, and A*h2 algorithms for 35 trials. For each trial the program will generate a new puzzle from the previous by a random move (note that the random move, even when making sure not to backtrack the previous move, does not necessarily increase the depth). The 3 algorithms are than ran on this puzzle various statistics recorded including solution depth, number of nodes used by the search tree, and runtime.

If an algorithm runs out of memory (observed by catching OutOfMemoryError exceptions) it is not performed in further trials. A table of the trials is then printed allowing the user to compare to the performance of the various algorithms. Note that the trial may take several minutes since it will wait for an algorithm to run out of memory.

The program also has an option to set the random seed (by default there is a random random seed). The random generator is used in the FifteenPuzzleGenerator class to create and modify game puzzles. By setting the random seed one can obtain reproducible results in various tests, as the sequence of numbers generated by the random generator is determined by the random seed.

## Test Files

Samples puzzles "1-deep", "2-deep", and "3-deep" are included

## Stat results

As we can see from the below sizes for the search tree and runtime show the performance of the algorithm. IDLS performs worst, running out of memory at a solution depth of only 10 while the A* algorithms are able to go above 20. Heuristic 2 (sum of the Manhattan distances) outperforms Heuristic 1 (out of place tiles), able to reach up to a solution depth of 28 before running out of memory, thus indicating h2 is a better heuristic.

Ex. Stat test (seed 42):

```                                        [depth/nodes/time]      
```
```Trial   (IDLS)                          (A*h1)                          (A*h2) 
```0       [0d/1n/2.633ms]                 [0d/1n/102.0µs]                 [0d/1n/60.0µs]           
```1       [1d/4n/225.0µs]                 [1d/4n/195.0µs]                 [1d/4n/328.0µs]          
```2       [2d/19n/242.0µs]                [2d/8n/235.0µs]                 [2d/8n/909.0µs]          
```3       [3d/22n/216.0µs]                [3d/13n/231.0µs]                [3d/13n/1.219ms]         
```4       [4d/65n/560.0µs]                [4d/20n/573.0µs]                [4d/15n/294.0µs]         
```5       [5d/202n/3.796ms]               [5d/41n/6.334ms]                [5d/18n/406.0µs]         
```6       [6d/475n/3.687ms]               [6d/44n/411.0µs]                [6d/21n/383.0µs]         
```7       [7d/477n/824.0µs]               [7d/83n/1.015ms]                [7d/23n/416.0µs]         
```8       [8d/12405n/71.151ms]            [8d/98n/885.0µs]                [8d/26n/3.325ms]         
```9       [9d/12409n/38.534ms]            [9d/191n/1.586ms]               [9d/41n/739.0µs]         
```10      [10d/185055n/937.437ms]         [10d/203n/1.634ms]              [10d/45n/754.0µs]        
```11      -                               [11d/156n/693.0µs]              [11d/48n/747.0µs]        
```12      -                               [12d/180n/1.488ms]              [12d/75n/1.322ms]         
```13      -                               [13d/413n/1.942ms]              [13d/99n/1.733ms]         
```14      -                               [14d/778n/3.488ms]              [14d/410n/8.493ms]        
```15      -                               [15d/1206n/5.141ms]             [15d/449n/10.942ms]       
```16      -                               [16d/1586n/7.253ms]             [16d/703n/17.35ms]        
```17      -                               [17d/2588n/12.204ms]            [17d/610n/15.134ms]       
```18      -                               [18d/6504n/32.884ms]            [18d/3049n/96.124ms]      
```19      -                               [19d/37069n/278.228ms]          [19d/5081n/185.691ms]     
```20      -                               [20d/170075n/1.772504secs]      [20d/28524n/1.210537secs] 
```21      -                               [21d/127733n/1.139556secs]      [21d/33419n/1.438142secs] 
```22      -                               -                               [22d/24249n/973.644ms]    
```23      -                               -                               [23d/89553n/4.074421secs] 
```24      -                               -                               [24d/46208n/1.987392secs] 
```25      -                               -                               [27d/162513n/7.608207secs]
```26      -                               -                               [26d/64764n/2.815847secs] 
```27      -                               -                               [27d/146610n/7.162246secs]
```28      -                               -                               [28d/116785n/5.431199secs]
```29      -                               -                               [27d/70060n/3.477923secs] 
```30      -                               -                               -                         
```31      -                               -                               -                         
```32      -                               -                               -                         
```33      -                               -                               -                         
```34      -                               -                               -                         
```35      -                               -                               -                         

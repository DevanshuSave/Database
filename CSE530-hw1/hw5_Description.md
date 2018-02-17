HW5 is an opportunity to improve or add on to your previous assignments in order to receive extra credit.[1]

The idea selected is Creating explicit Transaction objects and running multi-threaded transactions.[1]


## Implementation details

### Files created:
 - Actions: The class represents the actions which can be requested for a transaction (fetch page, read, write, complete/commit)
 - Transaction: This class maintains a transaction object which is and the execution expected in response to the list of actions(type: Trans, discussed in the next point) it receives 
 - DeadlockTest: Tests multiple scenarios of a transaction schedule. This class contains an innerclass Trans which contains the data required to pass to a transaction (Action, page, table, tuple)

## References

[1] http://www.cse.wustl.edu/~dshook/cse530/hw/hw5.html


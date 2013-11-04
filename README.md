Lift Item List example
----------------------

Run with sbt 0.12:

    $ sbt
    > ~;container:start; container:reload /

Then open your browser at http://localhost:8080

You see 3 identical lists of items.
It should be possible to add and remove items on each list independently.
On page reload the lists are identical again.

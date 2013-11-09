Lift Item List example
----------------------

Run with sbt 0.12:

    $ sbt
    > ~;container:start; container:reload /

Then open your browser at http://localhost:8080

In the first column you see a nested list of items.
It should be possible to add and remove items on each list independently.

In the second column you see some generated Ajax forms from the same template.
They shoud work independently from each other.

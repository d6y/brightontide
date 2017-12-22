Brighton Tide is a script to scrape the tide time information post them to Twitter,
as described in [a blog post of 2009](http://richard.dallaway.com/still-loving-the-scala.html).


Running the app
---------------

Run without args (`sbt run`) to see what would have been Tweeted without actually
tweeting it.

To actually tweet, run with a set of tokens:

    $ sbt "run consumer_key token_value consumer_secret access_token_secret"


Running the specs
-----------------

    $ sbt test

Sources
-------

* [Admiralty EasyTide](http://www.ukho.gov.uk/) (the United Kingdom Hydrographic Office) free tide predictions for Brighton Marina.
* [Visit Brighton Tidetimes](http://www.visitbrighton.com/plan-your-visit/tides)


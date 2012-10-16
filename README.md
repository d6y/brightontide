Brighton Tide is a script to scrape the tide time information
from VisitBrighton.com and post them to Twitter, as described in [a blog post of 2009](http://richard.dallaway.com/still-loving-the-scala).


Running the app
---------------

    $ sbt "run consumer_key token_value consumer_secret access_token_secret"

Run without args (`sbt run`) to see what would have been Tweeted without actually
tweeting it.

Running the specs
-----------------

    $ sbt test


History
-------

* Prior to 3 Jan 2011, the VisitBrightonScraper was used for tide data.  Unfortunately, each January the web site contained no data for a few days, which is inconvenient.  So from 3 Jan, Easy Tide was added as the live source.


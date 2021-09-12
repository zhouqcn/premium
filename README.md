# Premium

This program is to pick the best insurance products upon a quote request.

## Run the program

```
// Create a Quote object.
Quote quote = new Quote();

// Load premium data from config files.
Config config = Config.readConfig("test_products.json");
testQuote.loadDataFromConfig(config);

// Process the request and get the results.
List<QuoteResult> results = quote.processRequest(testRequest);
```

## Config file.

Need to put one products.json file under src/main/resources (For test, put test_products.json under src/test/resources).
This config file contains parameters and premium tables for insurer. Insurer name, class, gender must be defined in Enum.

The premium data are stored in csv files which under the same location as config json file. The file contains table for querying premiums with age and coverage amount.
Each csv file is for one insurer with given gender, term, premium class. One insurer would have multiple such tables.

The csv files shouldn't contain any string with ',', for example the coverage amount "$1,000,000" must be written as "$1000000".

All config and csv files will provided by FinMind Inc.

## Quote Request

The request object should be initialized with XML file. The XML file contains the wizard answers.

## Return

processRequest() returns a list of QuoteResult. It contains up to 3 results. Each result has insurer name and premium value. According to the insurer name, the front end could display more description about the insurer.
It is possible the quote result return empty list. For example if the insured has military service or high risk activity, we don't return result and the UI needs to let applicant contact FinMind.

## License




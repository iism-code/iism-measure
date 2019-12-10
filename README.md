# iism-measure
Measurement library for Java

#Usage

Given a function named processCustomerPurchase that you want to measure whenever it is executed you can use try:

    try (TimeMeasure timeMeasure = TimeMeasure.getOrCreateMeasure("Purchase Processing Time")) {
      processCustomerPurchase();
    }

Or just manually call startMeasure and endMeasure:

    TimeMeasure timeMeasure = TimeMeasure.getOrCreateMeasure("Purchase Processing Time");
    timeMeasure.startMeasure();
    processCustomerPurchase();
    timeMeasure.endMeasure();

To extract the data, call TimeMeasure.snapshotTimeMeasureData and output the records in whatever format you wish.  Note that outputting in XML can then be imported into Microsoft Excel pretty easily under the developer ribbon, and CSV works well for google sheets.

		HashSet<RollupData> rollupData = new HashSet<RollupData>();
		TimeMeasure.snapshotTimeMeasureData(rollupData);
    
    for(RollupData data : rollupData) {
      //Output the fields in data here
    }

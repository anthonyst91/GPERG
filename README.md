# Google-Play-Earning-Receipt-Generator

Generator of receipt for your monthly earning on Google Play.

The latest delivery can be found in the /application folder, including:
- a placeholder for google_receipt.csv
- the invoice_details.csv file
- the generate_receipt.sh script

## How to use

### Step 1

The Google Play reports are edited monthly by Google.
They are usually generated around the 3-4 of the month for the previous month's sales.

1. Go to the Google Play Console
2. From the left, open "Download reports" > "Financial"
3. In the "Earning reports" section, find the report you want

### Step 2

1. Unzip the report you're downloaded from Google Play Console
2. Rename the CSV file to: "google_receipt.csv"
3. Put this file close to the GooglePlayEarningsReportGenerator.jar app

### Step 3:

1. Edit the invoice_details.csv CSV file with your receipt metadata.
2. Launch the generate_receipt.sh script

## Analyze your receipt

Two receipts are generated:

1. gplay_earnings_receipt_basic.pdf : contains the summary of your monthly sales
2. gplay_earnings_receipt_full.pdf : contains the details of your monthly sales per region and countries

package com.streamlined.orderbook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Parser {

	private static final int INITIAL_CAPACITY = 1000;
	private static final String COMMA_SEPARATOR = ",";
	private static final String UPDATE_COMMAND = "u";
	private static final String QUERY_COMMAND = "q";
	private static final String OMIT_COMMAND = "o";

	private final File inputFile;
	private final File outputFile;
	private final OrderBook orderBook;

	public Parser(File file) {
		this.inputFile = file;
		outputFile = new File("output.txt");
		orderBook = new OrderBook(INITIAL_CAPACITY);
	}

	public void parse() throws IOException {

		try (FileReader fileReader = new FileReader(inputFile);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				FileWriter fileWriter = new FileWriter(outputFile);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

			while (bufferedReader.ready()) {

				final String[] values = bufferedReader.readLine().split(COMMA_SEPARATOR);
				final String command = values[0].trim();

				switch (command) {
				case UPDATE_COMMAND:
					updateOrderBook(values[1], values[2], values[3].trim());
					break;
				case QUERY_COMMAND:
					queryOrderBook(bufferedWriter, values);
					break;
				case OMIT_COMMAND:
					omitOrderBook(values[1].trim(), values[2]);
					break;
				default:
					System.err.println("unknown command " + command);
					break;
				}

			}
		}
	}

	private void updateOrderBook(String priceValue, String sizeValue, String type) {
		int price = Integer.parseInt(priceValue);
		int size = Integer.parseInt(sizeValue);

		switch (type) {
		case "bid":
			orderBook.setBidSize(price, size);
			break;
		case "ask":
			orderBook.setAskSize(price, size);
			break;
		default:
			System.err.println("wrong type of update " + type);
		}
	}

	private void queryOrderBook(BufferedWriter bufferedWriter, String[] values) throws IOException {
		final String type = values[1];

		switch (type) {
		case "best_bid":
			PriceVolume priceVolumeBid = orderBook.queryBestBid();
			if (priceVolumeBid == null) {
				System.out.println("no valid price and size for best bid");
			} else {
				bufferedWriter.write(String.format("%d,%d", priceVolumeBid.getPrice(), priceVolumeBid.getVolume()));
				bufferedWriter.newLine();
			}
			break;
		case "best_ask":
			PriceVolume priceVolumeAsk = orderBook.queryBestAsk();
			if (priceVolumeAsk == null) {
				System.out.println("no valid price and size for best ask");
			} else {
				bufferedWriter.write(String.format("%d,%d", priceVolumeAsk.getPrice(), priceVolumeAsk.getVolume()));
				bufferedWriter.newLine();
			}
			break;
		case "size":
			final int price = Integer.parseInt(values[2]);
			bufferedWriter.write(String.format("%d", orderBook.querySizeForPrice(price)));
			bufferedWriter.newLine();
			break;
		default:
			System.err.println("wrong type of query " + type);
		}
	}

	private void omitOrderBook(String type, String volumeValue) {
		int volume = Integer.parseInt(volumeValue);

		switch (type) {
		case "buy":
			orderBook.buyBestAsk(volume);
			break;
		case "sell":
			orderBook.sellBestBid(volume);
			break;
		default:
			System.err.println("wrong type of operation " + type);
		}
	}

	public static void main(String... args) {
		try {
			new Parser(new File("input.txt")).parse();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

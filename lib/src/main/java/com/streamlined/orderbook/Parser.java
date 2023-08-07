package com.streamlined.orderbook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Parser {

	private static final int INITIAL_CAPACITY = 45 * 1024 * 1024 / (4 * 2);// 128Mb heap size restriction
	private static final String COMMA_SEPARATOR = ",";
	private static final String UPDATE_COMMAND = "u";
	private static final String QUERY_COMMAND = "q";
	private static final String ORDER_COMMAND = "o";

	private final File inputFile;
	private final File outputFile;
	private final OrderBook orderBook;

	public Parser(File inputFile, File outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
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
					bufferedWriter.write(queryOrderBook(values));
					break;
				case ORDER_COMMAND:
					giveOrder(values[1].trim(), values[2]);
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

	private String queryOrderBook(String[] values) {
		final String type = values[1].trim();

		switch (type) {
		case "best_bid":
			PriceVolume priceVolumeBid = orderBook.queryBestBid();
			if (priceVolumeBid == null) {
				System.err.println("no valid price and size for best bid");
				break;
			}
			return String.format("%d,%d%n", priceVolumeBid.getPrice(), priceVolumeBid.getVolume());
		case "best_ask":
			PriceVolume priceVolumeAsk = orderBook.queryBestAsk();
			if (priceVolumeAsk == null) {
				System.err.println("no valid price and size for best ask");
				break;
			}
			return String.format("%d,%d%n", priceVolumeAsk.getPrice(), priceVolumeAsk.getVolume());
		case "size":
			final int price = Integer.parseInt(values[2]);
			return String.format("%d%n", orderBook.querySizeForPrice(price));
		default:
			System.err.println("wrong type of query " + type);
		}
		return "";
	}

	private void giveOrder(String type, String volumeValue) {
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
			new Parser(new File("input.txt"), new File("output.txt")).parse();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

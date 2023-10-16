package com.thuy.bill.payment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
	private List<Bill> bills;
	private List<Payment> payments;
	private int balance;

	public Main() {
		bills = new ArrayList<>(List.of(new Bill(1, "ELECTRIC", 200000, "25/10/2020", "NOT_PAID", "EVN HCMC"),
				new Bill(2, "WATER", 175000, "30/10/2020", "NOT_PAID", "SAVACO HCMC"),
				new Bill(3, "INTERNET", 800000, "30/11/2020", "NOT_PAID", "VNPT")));
		payments = new ArrayList<>();
		balance = 0;
	}

	public void processCommand(String command) {
		if (command.isBlank()) {
			return;
		}

		String[] parts = command.split(" ");

		switch (parts[0]) {
		case "CASH_IN": {
			if (parts.length == 2) {
				String amount = parts[1];
				try {
					int cash = Integer.valueOf(amount);
					if (cash > 0) {
						balance += cash;
					}
				} catch (Exception e) {
					System.out.println("The cash should be number and > 0");
				}
				System.out.println("Your available balance: " + balance);
			}
			break;
		}
		case "LIST_BILL": {
			String[] headers = { "Bill No.", "Type", "Amount", "Due Date", "State", "PROVIDER" };
			String[][] data = bills.stream()
					.map(bill -> new String[] { String.valueOf(bill.getId()), bill.getType(),
							String.valueOf(bill.getAmount()), bill.getDueDate(), bill.getState(), bill.getProvider() })
					.toArray(String[][]::new);
			exportTablePayment(headers, data);
			break;
		}
		case "PAY": {
			if (parts.length == 2) {
				payBillByBillId(parts[1]);
			} else if (parts.length > 2) {
				payBills(parts);
			}

			break;
		}
		case "DUE_DATE": {
			List<Bill> filterByProvider = bills.stream().filter(bill -> !"PROCESSED".equals(bill.getState()))
					.collect(Collectors.toList());

			String[] headers = { "No.", "Amount", "Payment Date", "State", "Bill Id" };
			String[][] data = filterByProvider.stream().map(bill -> new String[] { String.valueOf(bill.getId()),
					String.valueOf(bill.getAmount()), bill.getDueDate(), bill.getState(), bill.getProvider() })
					.toArray(String[][]::new);
			exportTablePayment(headers, data);
			break;
		}
		case "SCHEDULE": {
			if (parts.length == 3) {
				String billId = parts[1];
				String schedule = parts[2];

				Optional<Bill> result = bills.stream().filter(bill -> bill.getId() == Integer.valueOf(billId))
						.findFirst();

				if (result.isPresent()) {
					Bill bill = result.get();
					bill.setSate(schedule);
					System.out.println("Payment for bill id " + billId + " is scheduled on " + schedule);
				}
			}
			break;
		}
		case "LIST_PAYMENT": {
			String[] headers = { "No.", "Amount", "Payment Date", "State", "Bill Id" };
			String[][] data = payments.stream()
					.map(payment -> new String[] { String.valueOf(payment.getId()), String.valueOf(payment.getAmount()),
							payment.getPaymentDate(), payment.getState(), String.valueOf(payment.getBillId()) })
					.toArray(String[][]::new);
			exportTablePayment(headers, data);
			break;
		}
		case "SEARCH_BILL_BY_PROVIDER": {
			if (parts.length == 2) {
				String provider = parts[1];

				if (!provider.isBlank()) {
					List<Bill> filterByProvider = bills.stream().filter(bill -> provider.equals(bill.getProvider()))
							.collect(Collectors.toList());

					String[] headers = { "No.", "Amount", "Payment Date", "State", "Bill Id" };
					String[][] data = filterByProvider.stream().map(bill -> new String[] { String.valueOf(bill.getId()),
							String.valueOf(bill.getAmount()), bill.getDueDate(), bill.getState(), bill.getProvider() })
							.toArray(String[][]::new);
					exportTablePayment(headers, data);
				}
			}

			break;
		}
		default:
			break;
		}
	}

	private void exportTablePayment(String[] headers, String[][] data) {
		// Calculate the maximum width for each column
		int[] columnWidths = new int[headers.length];

		// Print the table headers
		for (int i = 0; i < headers.length; i++) {
			System.out.print(padRight(headers[i], columnWidths[i] + 20));
		}
		System.out.println();

		// Print the table data
		for (String[] row : data) {
			for (int i = 0; i < row.length; i++) {
				System.out.print(padRight(row[i], columnWidths[i] + 20));
			}
			System.out.println();
		}
	}

	private static String padRight(String s, int length) {
		return String.format("%-" + length + "s", s);
	}

	private void payBillByBillId(String billId) {
		Optional<Bill> result = bills.stream().filter(bill -> bill.getId() == Integer.valueOf(billId)).findFirst();

		if (result.isPresent()) {
			Bill bill = result.get();
			payBill(bill, billId, bill.getAmount() <= balance);
			return;
		}
		System.out.println("Sorry! Not found a bill with such id " + billId);

	}

	private void payBill(Bill bill, String billId, boolean canPaid) {
		if (canPaid && bill.getAmount() <= balance) {
			balance -= bill.getAmount();
			bill.markAsPaid(payments);
			System.out.println("Payment has been completed for Bill with id " + billId);
			System.out.println("Your current balance is: " + balance);
		} else {
			bill.markAsUnPaid(payments);
		}

	}

	private void payBills(String[] parts) {
		int totalBill = 0;
		Map<String, Bill> mBill = new HashMap<>();
		for (int i = 1; i < parts.length; i++) {
			String billId = parts[i];

			Optional<Bill> result = bills.stream().filter(bill -> bill.getId() == Integer.valueOf(billId)).findFirst();

			if (!result.isPresent()) {
				System.out.println("Sorry! Not found a bill with such id " + billId);
				return;
			}

			Bill bill = result.get();
			totalBill += bill.getAmount();
			mBill.put(billId, bill);
		}

		boolean canPaid = totalBill <= balance;
		if (!canPaid) {
			System.out.println("Sorry! Not enough fund to proceed with payment.");
		}
		for (Entry<String, Bill> entry : mBill.entrySet()) {
			payBill(entry.getValue(), entry.getKey(), canPaid);
		}
	}

	public static void main(String[] args) {
		Main paymentSystem = new Main();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.print("$ ");
			String command = scanner.nextLine();
			if (command.equals("EXIT")) {
				System.out.println("Good bye");
				break;
			} else {
				paymentSystem.processCommand(command);
			}
		}

		scanner.close();
	}
}

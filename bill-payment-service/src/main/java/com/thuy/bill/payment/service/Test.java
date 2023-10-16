package com.thuy.bill.payment.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Test {
	public static void main(String[] args) {

		// Redirect standard output to capture it
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));

		Main billPaymentSystem = new Main();

		// CASH_IN
		testCashIn(billPaymentSystem, output);

		// LIST_BILL
		testListBill(billPaymentSystem, output);

		// PAY
		testPay(billPaymentSystem, output);

		// PAY 2 3
		testPayMulti(billPaymentSystem, output);

		// Due date
		testDueDate(billPaymentSystem, output);

		// SCHEDULE
		testSchedule(billPaymentSystem, output);

		// LIST_PAYMENT
		testListPayMent(billPaymentSystem, output);

		// SEARCH_BILL_BY_PROVIDER
		testSearchBillByProvider(billPaymentSystem, output);

	}

	private static void testSearchBillByProvider(Main billPaymentSystem, ByteArrayOutputStream output) {
		billPaymentSystem.processCommand("SEARCH_BILL_BY_PROVIDER VNPT");

		String systemSearchBillByProvider = output.toString();
		if (systemSearchBillByProvider.contains(
				"No.                 Amount              Payment Date        State               Bill Id             \r\n"
						+ "3                   800000              30/11/2020          PENDING             VNPT                ")) {
			System.err.println("Test Passed SEARCH_BILL_BY_PROVIDER: " + systemSearchBillByProvider);
		} else {
			System.err.println("Test Failed SEARCH_BILL_BY_PROVIDER: " + systemSearchBillByProvider);
		}

	}

	private static void testListPayMent(Main billPaymentSystem, ByteArrayOutputStream output) {
		billPaymentSystem.processCommand("LIST_PAYMENT");

		String systemListPayment = output.toString();
		if (systemListPayment.contains("Payment for bill id 2 is scheduled on 28/10/2020")) {
			System.err.println("Test Passed LIST_PAYMENT: " + systemListPayment);
		} else {
			System.err.println("Test Failed LIST_PAYMENT: " + systemListPayment);
		}
	}

	private static void testSchedule(Main billPaymentSystem, ByteArrayOutputStream output) {
		billPaymentSystem.processCommand("SCHEDULE 2 28/10/2020");

		String systemSchedule = output.toString();
		if (systemSchedule.contains("Payment for bill id 2 is scheduled on 28/10/2020")) {
			System.err.println("Test Passed SCHEDULE: " + systemSchedule);
		} else {
			System.err.println("Test Failed SCHEDULE: " + systemSchedule);
		}
	}

	private static void testDueDate(Main billPaymentSystem, ByteArrayOutputStream output) {
		billPaymentSystem.processCommand("DUE_DATE");

		String systemDueDate = output.toString();
		if (systemDueDate.contains(
				"No.                 Amount              Payment Date        State               Bill Id             \r\n"
						+ "2                   175000              30/10/2020          PENDING             SAVACO HCMC         \r\n"
						+ "3                   800000              30/11/2020          PENDING             VNPT                ")) {
			System.err.println("Test Passed DUE_DATE: \r\n" + systemDueDate);
		} else {
			System.err.println("Test Failed DUE_DATE: \r\n" + systemDueDate);
		}
	}

	private static void testPayMulti(Main billPaymentSystem, ByteArrayOutputStream output) {
		billPaymentSystem.processCommand("PAY 2 3");

		String systemPayMulti = output.toString();
		if (systemPayMulti.contains("Sorry! Not enough fund to proceed with payment.")) {
			System.err.println("Test Passed PAY MULTI: " + systemPayMulti);
		} else {
			System.err.println("Test Failed PAY MULTI: " + systemPayMulti);
		}

	}

	private static void testPay(Main billPaymentSystem, ByteArrayOutputStream output) {
		billPaymentSystem.processCommand("PAY 1");

		String systemPay = output.toString();
		if (systemPay
				.contains("Payment has been completed for Bill with id 1\r\n" + "Your current balance is: 800000")) {
			System.err.println("Test Passed PAY: " + systemPay);
		} else {
			System.err.println("Test Failed PAY: " + systemPay);
		}
	}

	private static void testListBill(Main billPaymentSystem, ByteArrayOutputStream output) {
		billPaymentSystem.processCommand("LIST_BILL");

		String systemOutputListBill = output.toString();
		if (systemOutputListBill.contains("Your available balance: 1000000123\r\n"
				+ "Bill No.            Type                Amount              Due Date            State               PROVIDER            \r\n"
				+ "1                   ELECTRIC            200000              25/10/2020          NOT_PAID            EVN HCMC            \r\n"
				+ "2                   WATER               175000              30/10/2020          NOT_PAID            SAVACO HCMC         \r\n"
				+ "3                   INTERNET            800000              30/11/2020          NOT_PAID            VNPT                \r\n")) {
			System.err.println("Test Passed LIST_BILL: " + systemOutputListBill);
		} else {
			System.err.println("Test Failed LIST_BILL: " + systemOutputListBill);
		}
	}

	private static void testCashIn(Main billPaymentSystem, ByteArrayOutputStream output) {
		billPaymentSystem.processCommand("CASH_IN 2000000");

		String systemOutputCashIn = output.toString();
		if (systemOutputCashIn.contains("Your available balance: 2000000")) {
			System.err.println("Test Passed CASH_IN: " + systemOutputCashIn);
		} else {
			System.err.println("Test Failed CASH_IN: " + systemOutputCashIn);
		}
	}
}

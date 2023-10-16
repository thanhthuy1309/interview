package com.thuy.bill.payment.service;

public class Payment {
	private int id;
	private int amount;
	private String paymentDate;
	private String state;
	private int billId;

	public Payment(int id, int amount, String paymentDate, String state, int billId) {
		this.id = id;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.state = state;
		this.billId = billId;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public String getState() {
		return state;
	}

	public int getBillId() {
		return billId;
	}
}
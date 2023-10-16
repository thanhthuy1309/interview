package com.thuy.bill.payment.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Bill {
	private int id;
	private String type;
	private int amount;
	private String dueDate;
	private String state;
	private String provider;
	private String schedule;

	public Bill(int id, String type, int amount, String dueDate, String state, String provider) {
		this.id = id;
		this.type = type;
		this.amount = amount;
		this.dueDate = dueDate;
		this.state = "NOT_PAID";
		this.provider = provider;
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public int getAmount() {
		return amount;
	}

	public String getDueDate() {
		return dueDate;
	}

	public String getState() {
		return state;
	}

	public String getProvider() {
		return provider;
	}

	public void markAsPaid(List<Payment> payments) {
		this.state = "PROCESSED";

		payments.add(getPayment(payments.size() + 1));
	}

	public void markAsUnPaid(List<Payment> payments) {
		this.state = "PENDING";

		payments.add(getPayment(payments.size() + 1));
	}

	private Payment getPayment(int paymentId) {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedDate = currentDate.format(formatter);

		return new Payment(paymentId, amount, formattedDate, state, id);
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSate(String schedule) {
		this.schedule = schedule;
	}
}

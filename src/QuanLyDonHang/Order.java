package QuanLyDonHang;

import java.sql.Date;

public class Order {
    private int id;
    private int customerId;
    private Date orderDate;
    private double totalAmount;

    public Order(int customerId, Date orderDate, double totalAmount) {
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public Date getOrderDate() { return orderDate; }
    public double getTotalAmount() { return totalAmount; }
}
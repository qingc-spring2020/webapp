package com.csye6225.assignment3.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class Bill implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.bill_id
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private String billId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.created_ts
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private Date createdTs;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.updated_ts
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private Date updatedTs;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.owner_id
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private String ownerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.vendor
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private String vendor;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.bill_date
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private Date billDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.due_date
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private Date dueDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.amount_due
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private Double amountDue;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.categories
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private String categories;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.payment_status
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private String paymentStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bill.file_id
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private String fileId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table bill
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.bill_id
     *
     * @return the value of bill.bill_id
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public String getBillId() {
        return billId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.bill_id
     *
     * @param billId the value for bill.bill_id
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setBillId(String billId) {
        this.billId = billId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.created_ts
     *
     * @return the value of bill.created_ts
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public Date getCreatedTs() {
        return createdTs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.created_ts
     *
     * @param createdTs the value for bill.created_ts
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setCreatedTs(Date createdTs) {
        this.createdTs = createdTs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.updated_ts
     *
     * @return the value of bill.updated_ts
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public Date getUpdatedTs() {
        return updatedTs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.updated_ts
     *
     * @param updatedTs the value for bill.updated_ts
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setUpdatedTs(Date updatedTs) {
        this.updatedTs = updatedTs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.owner_id
     *
     * @return the value of bill.owner_id
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.owner_id
     *
     * @param ownerId the value for bill.owner_id
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.vendor
     *
     * @return the value of bill.vendor
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.vendor
     *
     * @param vendor the value for bill.vendor
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.bill_date
     *
     * @return the value of bill.bill_date
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public Date getBillDate() {
        return billDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.bill_date
     *
     * @param billDate the value for bill.bill_date
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.due_date
     *
     * @return the value of bill.due_date
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.due_date
     *
     * @param dueDate the value for bill.due_date
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.amount_due
     *
     * @return the value of bill.amount_due
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public Double getAmountDue() {
        return amountDue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.amount_due
     *
     * @param amountDue the value for bill.amount_due
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.categories
     *
     * @return the value of bill.categories
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public String getCategories() {
        return categories;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.categories
     *
     * @param categories the value for bill.categories
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setCategories(String categories) {
        this.categories = categories;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.payment_status
     *
     * @return the value of bill.payment_status
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.payment_status
     *
     * @param paymentStatus the value for bill.payment_status
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bill.file_id
     *
     * @return the value of bill.file_id
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bill.file_id
     *
     * @param fileId the value for bill.file_id
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    public void setFileId(String fileId) {
        if(fileId.equals(""))
            this.fileId = null;
        else
            this.fileId = fileId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bill
     *
     * @mbg.generated Tue Feb 11 22:53:22 EST 2020
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", billId=").append(billId);
        sb.append(", createdTs=").append(createdTs);
        sb.append(", updatedTs=").append(updatedTs);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", vendor=").append(vendor);
        sb.append(", billDate=").append(billDate);
        sb.append(", dueDate=").append(dueDate);
        sb.append(", amountDue=").append(amountDue);
        sb.append(", categories=").append(categories);
        sb.append(", paymentStatus=").append(paymentStatus);
        sb.append(", fileId=").append(fileId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
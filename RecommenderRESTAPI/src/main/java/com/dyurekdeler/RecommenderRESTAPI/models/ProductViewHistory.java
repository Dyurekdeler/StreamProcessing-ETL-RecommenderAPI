package com.dyurekdeler.RecommenderRESTAPI.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "product_view_history")
public class ProductViewHistory {

    @Column(name = "product_id")
    private String productId;

    @Column(name = "event")
    private String event;

    @Id
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "source")
    private String source;

    @Column(name = "click_timestamp")
    private Timestamp clickTimestamp;

    public ProductViewHistory() {

    }

    public ProductViewHistory(String productId, String event, String messageId, String userId, String source, Timestamp clickTimestamp){
        this.clickTimestamp = clickTimestamp;
        this.event = event;
        this.messageId = messageId;
        this.userId = userId;
        this.source = source;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Timestamp getClickTimestamp() {
        return clickTimestamp;
    }

    public void setClickTimestamp(Timestamp clickTimestamp) {
        this.clickTimestamp = clickTimestamp;
    }
}

package com.bryan.spaceinvader.model.shop;

import com.bryan.spaceinvader.model.player.Upgrade;
import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class ShopItemNode extends VBox {
    // TODO passer ça dans un fichier css et faire un fichier css / fxml + un fichier global

    private final ShopItem shopItem;
    private final ImageView imageView;
    private final Label costLabel;
    private Tooltip tooltip;

    public ShopItemNode(ShopItem shopItem) {
        this.shopItem = shopItem;

        // Load the image
        imageView = new ImageView(ResourceManager.loadResource(shopItem.getType().getTexture(), Image.class, ResourceType.IMAGE));
        imageView.setFitWidth(70); // Set the width of the image
        imageView.setFitHeight(70); // Set the height of the image

        tooltip = new Tooltip(shopItem.getDescription());
        Tooltip.install(this, tooltip);

        // Create a label for the cost
        costLabel = new Label();
        setCostPrice(shopItem.getCost());
        costLabel.setFont(new Font("Arial", 14));
        costLabel.setTextFill(shopItem.isLocked() ? Paint.valueOf("RED") : Paint.valueOf("WHITE"));

        // Set spacing and alignment
        this.setSpacing(10); // Space between image and cost label
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10)); // Padding around the VBox

        // Add the image and cost label to the VBox
        this.getChildren().addAll(imageView, costLabel);
    }

    public ShopItem getShopItem() {
        return shopItem;
    }

    private void setCostPrice(int cost) {
        this.costLabel.setText("Prix: " + cost);
    }

    public Upgrade handlePurchase() {
        Upgrade upgrade = shopItem.handlePurchase();
        refresh();
        return upgrade;
    }

    private void refresh() {
        setCostPrice(shopItem.getCost());
        Tooltip.uninstall(this, tooltip);
        tooltip = new Tooltip(shopItem.getDescription());
        Tooltip.install(this, tooltip);
        this.getChildren().clear();
        this.getChildren().addAll(imageView, costLabel);
    }
}

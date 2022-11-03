package com.example.demo3;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class ImagePatternCreator {

    public static ImagePattern createRectanglePattern() {
    	
        final double width        = 12;
        final double height       = 12;
        final Canvas canvas       = new Canvas(width, height);
        final GraphicsContext ctx = canvas.getGraphicsContext2D();

        ctx.setFill(Color.DODGERBLUE);
        ctx.fillRect(0, 0, width/2, height/2); // top left
        ctx.fillRect(width/2, height/2, width, height); // bottom right

        ctx.setFill(Color.DODGERBLUE.deriveColor(1, 1, 1, 0.85));
        ctx.fillRect(width/2, 0, width, height/2); // top right
        ctx.fillRect(0, height/2, width/2, height); // bottom left

        final Image PATTERN_IMAGE = canvas.snapshot(new SnapshotParameters(), null);
        final ImagePattern PATTERN = new ImagePattern(PATTERN_IMAGE, 0, 0, width, height, false);

        return PATTERN;
    }	
    
    public static ImagePattern createDiamondPattern() {
    	
        final double width        = 12;
        final double height       = 12;
        final Canvas canvas       = new Canvas(width, height);
        final GraphicsContext ctx = canvas.getGraphicsContext2D();

        ctx.setFill(Color.DODGERBLUE);
        ctx.fillRect(0, 0, width, height); // full area
        ctx.setFill(Color.SKYBLUE);
        ctx.fillPolygon( new double[] { width/2, width, width/2, 0}, new double[] { 0, height/2, height, height/2}, 4); // diamond
        
        final Image PATTERN_IMAGE = canvas.snapshot(new SnapshotParameters(), null);
        final ImagePattern PATTERN = new ImagePattern(PATTERN_IMAGE, 0, 0, width, height, false);

        return PATTERN;
    }
}

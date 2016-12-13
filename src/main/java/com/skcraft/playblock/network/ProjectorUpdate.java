package com.skcraft.playblock.network;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.network.Packet;

import java.io.IOException;

import com.sk89q.forge.Payload;

public class ProjectorUpdate implements Payload {

    private String uri;
    private float height;
    private float width;
    private float triggerRange;
    private float fadeRange;

    public ProjectorUpdate() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getTriggerRange() {
        return triggerRange;
    }

    public void setTriggerRange(float triggerRange) {
        this.triggerRange = triggerRange;
    }

    public float getFadeRange() {
        return fadeRange;
    }

    public void setFadeRange(float fadeRange) {
        this.fadeRange = fadeRange;
    }

    @Override
    public void read(ByteBufInputStream in) throws IOException {
        setUri(((ByteBufInputStream) in).readUTF());
        setWidth(((ByteBufInputStream) in).readFloat());
        setHeight(((ByteBufInputStream) in).readFloat());
        setTriggerRange(((ByteBufInputStream) in).readFloat());
        setFadeRange(((ByteBufInputStream) in).readFloat());
    }

    @Override
    public void write(ByteBufOutputStream out) throws IOException {
        ((ByteBufOutputStream) out).writeUTF(getUri());
        ((ByteBufOutputStream) out).writeFloat(getWidth());
        ((ByteBufOutputStream) out).writeFloat(getHeight());
        ((ByteBufOutputStream) out).writeFloat(getTriggerRange());
        ((ByteBufOutputStream) out).writeFloat(getFadeRange());
    }

}

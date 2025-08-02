package com.denizenscript.denizen.utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.PositionImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.CallbackI;

public class Location implements Cloneable {
    private double x;
    private double y;
    private double z;
    public ServerLevel serverLevel;
    float XRotation;
    float YRotation;
    public Level world;
    public Location (Level World, double initialx, double initialy, double initialz) {
    x = initialx;
    y = initialy;
    z = initialz;
    world = World;
    XRotation = 0;
    YRotation = 0;
    }

    public Location (Level World,double initialx, double initialy, double initialz, float pitch, float yaw) {
        x = initialx;
        y = initialy;
        z = initialz;
        XRotation = pitch;
        YRotation = yaw;
        world = World;
    }

    public Location (Level World, BlockPos pos)
    {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        world = World;
        XRotation = 0;
        YRotation = 0;
    }

    public Location(Vec3 vector)
    {
        x = vector.x();
        y = vector.y();
        z = vector.z();

    }

    public BlockPos toBlockPos()
    {
        return new BlockPos(x,y,z);
    }

    @Override
    public Location clone() {
        final Location clone;
        try {
            clone = (Location) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException("superclass messed up", ex);
        }
        clone.x = x;
        clone.y = y;
        clone.z = z;
        clone.XRotation = XRotation;
        clone.YRotation = YRotation;
        clone.world = world;
        return clone;
    }

    public Location add(double addX, double addY, double addZ)
    {
        return new Location(world, x + addX, y + addY, z + addZ, XRotation, YRotation);
    }

    public Location add(Vec3 addVec)
    {
        return new Location(world, x + addVec.x, y + addVec.y, z + addVec.z, XRotation, YRotation);
    }

    public Location add(Location addLocation)
    {
        return new Location(world, x + addLocation.getX(), y + addLocation.getY(), z + addLocation.getZ(), XRotation, YRotation);
    }

    public Location subtract(double addX, double addY, double addZ)
    {
        return new Location(world, x - addX, y - addY, z - addZ, XRotation, YRotation);
    }

    public Location subtract(Vec3 addVec)
    {
        return new Location(world, x - addVec.x, y - addVec.y, z - addVec.z, XRotation, YRotation);
    }

    public Location subtract(Location addLocation)
    {
        return new Location(world, x - addLocation.getX(), y - addLocation.getY(), z - addLocation.getZ(), XRotation, YRotation);
    }


    public void ConvertToIntegerLocation()
    {
        x = Math.floor(x);
        y = Math.floor(y);
        z = Math.floor(z);
    }

    public Block getBlock()
    {
        return this.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public BlockPos getBlockPos()
    {
        return new BlockPos(getBlockX(), getBlockY(), getBlockY());
    }

    public BlockState getBlockState()
    {
        return this.world.getBlockState(new BlockPos(x, y, z));
    }

    public double getX()
    {
        return x;
    }
    public double getY()
    {
        return y;
    }
    public double getZ()
    {
        return z;
    }
    public float getPitch()
    {
        return XRotation;
    }

    public float getYaw()
    {
        return YRotation;
    }

    public void setX(double newx)
    {
        x = newx;
    }

    public void setY(double newy)
    {
        y = newy;
    }

    public void setZ(double newz)
    {
        z = newz;
    }

    public void SetPitch(float newPitch)
    {
        XRotation = newPitch;
    }

    public void SetYaw(float newYaw)
    {
        YRotation = newYaw;
    }

    public Position toPosition()
    {
        return new PositionImpl(Math.round(x), Math.round(y), Math.round(z));
    }

    public Level getWorld()
    {
        return world;
    }

    public void setPitch(float pitch) {
        XRotation = pitch;
    }

    public void setYaw(float yaw) {
        YRotation = yaw;
    }

    public Location multiply(double input) {
        Location newlocation = this.clone();
        newlocation.x *= input;
        newlocation.y *= input;
        newlocation.z *= input;
        return  newlocation;
    }

    public void setWorld(Level newWorld){
        world = newWorld;
    }

    public Vec3 toVector()
    {
        return new Vec3(x, y, z);
    }

    public int getBlockY()
    {
        return (int)y;
    }

    public int getBlockZ()
    {
        return (int)z;
    }

    public int getBlockX()
    {
        return (int)x;
    }
}

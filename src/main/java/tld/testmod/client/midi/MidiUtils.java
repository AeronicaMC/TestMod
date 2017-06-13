package tld.testmod.client.midi;

import javax.annotation.Nullable;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tld.testmod.ModLogger;
import tld.testmod.network.PacketDispatcher;
import tld.testmod.network.server.ActiveReceiverMessage;

public enum MidiUtils implements Receiver
{
    //private static final MidiUtils INSTANCE = new MidiUtils();
    INSTANCE;

    static MidiDevice device;
    static MidiDevice.Info[] infos;
    static IActiveNoteReceiver instrument;
    static World world;
    static BlockPos pos = null;
    static IBlockState state;
    static EntityPlayer player;
    static EnumHand hand;
    static EnumFacing facing;
    static float hitX, hitY, hitZ;
    static ItemStack stack = ItemStack.EMPTY;

    public void getMidiIn(IActiveNoteReceiver instrumentIn, World worldIn, @Nullable BlockPos posIn, @Nullable IBlockState stateIn, EntityPlayer playerIn, EnumHand handIn, @Nullable EnumFacing facingIn, float hitXIn, float hitYIn, float hitZIn)
    {
        instrument = instrumentIn;
        world = worldIn;
        pos = posIn;
        state = stateIn;
        player = playerIn;
        hand = handIn;
        facing = facingIn;
        hitX = hitXIn;
        hitY = hitYIn;
        hitZ = hitZIn;
        
        infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++)
        {
            try
            {
                device = MidiSystem.getMidiDevice(infos[i]);
                // does the device have any transmitters?
                // if it does, add it to the device list
                if (device.isOpen()) device.close();
                ModLogger.info("%s, %d", infos[i], device.getMaxTransmitters());


                if (device.getMaxTransmitters() != 0 && !(device instanceof Sequencer))
                {
                    Transmitter trans = device.getTransmitter();
                    trans.setReceiver(getReceiver());

                    // open each device
                    device.open();
                    // if code gets this far without throwing an exception
                    // print a success message
                    ModLogger.info("%s was opened", device.getDeviceInfo());
                }

            } catch (MidiUnavailableException e)
            {
                ModLogger.error(e);
            }

        }
    }

    public void getMidiIn(IActiveNoteReceiver instrumentIn, World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        getMidiIn(instrumentIn, worldIn, playerIn.getPosition(), null, playerIn, handIn, null, 0, 0, 0);
 
    }

    
    public Receiver getReceiver()
    {
        return this;
    }
    
    @Override
    public void send(MidiMessage msg, long timeStamp)
    {
        byte[] message = msg.getMessage();
        if ((msg.getStatus() & 0xF0) == ShortMessage.NOTE_ON && pos != null)
        {
            ActiveReceiverMessage packet =  new ActiveReceiverMessage(pos, player.getEntityId(), message[1], message[2]);
            PacketDispatcher.sendToServer(packet);
                ModLogger.info("  msg: %x, %x, %x, %d", msg.getStatus() ,message[1], message[2] ,timeStamp);
        }
    }

    @Override
    public void close()
    {
        // TODO Auto-generated method stub
    }

    public void notifyRemoved(World worldIn, BlockPos posIn)
    {
        if (pos != null && pos.equals(posIn))
        {
            ModLogger.info("ActiveNoteReceiver Removed: %s", posIn);
            pos = null;
            stack = ItemStack.EMPTY;
        }
    }
    
    public void notifyRemoved(World worldIn, ItemStack stackIn)
    {
        if (!stack.equals(ItemStack.EMPTY) && stackIn.equals(stack))
        {
            ModLogger.info("ActiveNoteReceiver Removed: %s", stackIn.getDisplayName());
            pos = null;
            stack = ItemStack.EMPTY;
        }        
    }

}

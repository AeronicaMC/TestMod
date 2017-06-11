package tld.testmod.client.midi;

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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tld.testmod.ModLogger;

public enum MidiUtils implements Receiver
{
    //private static final MidiUtils INSTANCE = new MidiUtils();
    INSTANCE;

    static MidiDevice device;
    static MidiDevice.Info[] infos;
    static IMidiIn instrument;
    static World world;
    static BlockPos pos;
    static IBlockState state;
    static EntityPlayer player;
    static EnumHand hand;
    static EnumFacing facing;
    static float hitX, hitY, hitZ;

    public void getMidiIn(IMidiIn instrumentIn, World worldIn, BlockPos posIn, IBlockState stateIn, EntityPlayer playerIn, EnumHand handIn, EnumFacing facingIn, float hitXIn, float hitYIn, float hitZIn)
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

    public Receiver getReceiver()
    {
        return this;
    }
    
    @Override
    public void send(MidiMessage msg, long timeStamp)
    {
        byte[] message = msg.getMessage();
        if ((msg.getStatus() & 0xF0) == ShortMessage.NOTE_ON)
        {
            instrument.midiSend(world, pos, state, player, msg);
            for (byte b: message)
                ModLogger.info("  msg: %x, %x, %x, %d", msg.getStatus() ,msg.getLength(), b ,timeStamp);
        }
    }

    @Override
    public void close()
    {
        // TODO Auto-generated method stub
    }
 
}

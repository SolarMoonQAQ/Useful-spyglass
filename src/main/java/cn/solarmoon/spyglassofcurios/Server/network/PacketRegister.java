package cn.solarmoon.spyglassofcurios.Server.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static cn.solarmoon.spyglassofcurios.SpyglassOfCuriosMod.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PacketRegister {

    private static SimpleChannel instance;
    private int packetID = 0;

    private int id() {
        return packetID++;
    }

    public void register() {

        SimpleChannel network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "main"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(string -> true)
                .serverAcceptedVersions(string -> true)
                .simpleChannel();

        instance = network;

        network.messageBuilder(SpyglassUsePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SpyglassUsePacket::decode)
                .encoder(SpyglassUsePacket::encode)
                .consumer(SpyglassUsePacket::handle)
                .add();
    }

    public static void sendPacket(double multiplier, String renderType, String handle) {
        instance.sendToServer(new SpyglassUsePacket(multiplier, renderType, handle));
    }

    //注册数据包
    @SubscribeEvent
    public static void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {
        PacketRegister dataPackRegister = new PacketRegister();
        dataPackRegister.register();
    }

}
package io.github.imspacelover.growyourcrystal.client.ModBlockEntityRenderers;

import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalSeedBlockEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;


public class CrystalSeedBlockEntityRenderer implements BlockEntityRenderer<CrystalSeedBlockEntity> {

	public CrystalSeedBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

	}

	@Override
	public void render(CrystalSeedBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {

	}

//	@Override
//	public void render(CrystalSeedBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
//		matrices.push();
////		BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
//	}
}

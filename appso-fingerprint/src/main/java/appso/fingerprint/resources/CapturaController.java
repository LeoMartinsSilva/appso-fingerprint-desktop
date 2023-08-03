package appso.fingerprint.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.nitgen.SDK.BSP.NBioBSPJNI;

@Path("/captura")
public class CapturaController {
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/capturar")
	public Response capturar() {
		NBioBSPJNI bsp = new NBioBSPJNI();
		
		NBioBSPJNI.DEVICE_ENUM_INFO deviceEnumInfo = bsp.new DEVICE_ENUM_INFO();
		bsp.EnumerateDevice(deviceEnumInfo);
		
		if(deviceEnumInfo.DeviceInfo.length==0) {
			return Response.status(Status.BAD_REQUEST).entity("Nenhum dispositivo conectado").build();
		}
		
		bsp.OpenDevice(deviceEnumInfo.DeviceInfo[0].NameID, deviceEnumInfo.DeviceInfo[0].Instance);
		
		NBioBSPJNI.FIR_HANDLE firCap = bsp.new FIR_HANDLE();
		
		bsp.Capture(firCap);
		NBioBSPJNI.FIR_TEXTENCODE textSavedFIR = bsp.new FIR_TEXTENCODE(); 
		if(bsp.IsErrorOccured() == false) {
			bsp.GetTextFIRFromHandle(firCap, textSavedFIR);
		}
		/*Map<String, Object> retorno = new HashMap<String, Object>();
		
		retorno.put("id", textSavedFIR.TextFIR);*/
		
		bsp.CloseDevice(deviceEnumInfo.DeviceInfo[0].NameID, deviceEnumInfo.DeviceInfo[0].Instance);
		
		bsp.dispose();
		bsp = null;
		
		return Response.ok(textSavedFIR.TextFIR).build();
	}
	
	
	
	
}

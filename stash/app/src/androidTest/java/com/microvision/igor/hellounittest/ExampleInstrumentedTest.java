package com.microvision.igor.hellounittest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import picop.interfaces.def.ALC_Api;
import picop.interfaces.def.PicoP_Color;
import picop.interfaces.def.PicoP_ConnectionInfo;
import picop.interfaces.def.PicoP_ConnectionTypeE;
import picop.interfaces.def.PicoP_Handle;
import picop.interfaces.def.PicoP_LibraryInfo;
import picop.interfaces.def.PicoP_Point;
import picop.interfaces.def.PicoP_RC;
import picop.interfaces.def.PicoP_RectSize;
import picop.interfaces.def.PicoP_RenderTargetE;
import picop.interfaces.def.PicoP_TestPatternInfoE;
import picop.interfaces.def.PicoP_Ulog;

import static org.junit.Assert.*;
import static picop.interfaces.def.PicoP_ConnectionTypeE.eUSB;
import static picop.interfaces.def.UsbPortOperator.mContext;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        PicoP_Ulog.d("TAG", "Message");

        assertEquals("com.microvision.igor.hellounittest", appContext.getPackageName());
    }

    @Test
    public void openPicoPJar() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        PicoP_LibraryInfo libraryInfo = new PicoP_LibraryInfo();
        PicoP_Handle connectionHandle = new PicoP_Handle(eUSB);
        PicoP_ConnectionInfo connectionInfo = connectionHandle.getConnectInfo();
        connectionInfo.setConnectionContext(appContext);


        PicoP_RC ret = ALC_Api.PicoP_ALC_OpenLibrary(connectionHandle);
        assertEquals(PicoP_RC.eSUCCESS, ret);

        ret = ALC_Api.PicoP_ALC_OpenConnection(connectionHandle, PicoP_ConnectionTypeE.eUSB, connectionInfo);
        assertEquals(PicoP_RC.eSUCCESS, ret);

        ///////////////////////

        PicoP_RenderTargetE target = PicoP_RenderTargetE.eFRAME_BUFFER_0;
        PicoP_Point startPoint = new PicoP_Point();
        startPoint.setPicoP_Point((short)0, (short)0);
        PicoP_RectSize size = new PicoP_RectSize();
        size.setPicoP_RectSize((short)1024, (short)720);

        PicoP_Color foregroundColor = new PicoP_Color();
        foregroundColor.R = (byte)0x00;
        foregroundColor.G = (byte)0x99;
        foregroundColor.B = (byte)0x99;
        foregroundColor.A = (byte)0x00;

        PicoP_Color backgroundColor = new PicoP_Color();
        backgroundColor.R = (byte)0x00;
        backgroundColor.G = (byte)0x00;
        backgroundColor.B = (byte)0x00;
        backgroundColor.A = (byte)0x00;

        PicoP_TestPatternInfoE pattern = PicoP_TestPatternInfoE.eCHECKER_BOARD_PATTERN;

        for(int i = 0; i<250; i++){
            // todo: sleep between commands
            Thread.sleep(1000);
            ret = ALC_Api.PicoP_ALC_DrawTestPattern(connectionHandle, target, startPoint, size, foregroundColor, backgroundColor, pattern);
            assertTrue("hung after " + i + " commands", ret == PicoP_RC.eSUCCESS || ret == PicoP_RC.eRETRY);
        }
    }

    // TODO: SEND THIS c0000210161d1d0b2c00000000000004d0020099990000000000005f

    @Test
    public void sendBadCommand() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();


        PicoP_LibraryInfo libraryInfo = new PicoP_LibraryInfo();
        PicoP_Handle connectionHandle = new PicoP_Handle(eUSB);
        PicoP_ConnectionInfo connectionInfo = connectionHandle.getConnectInfo();
        connectionInfo.setConnectionContext(appContext);


        PicoP_RC ret = ALC_Api.PicoP_ALC_OpenLibrary(connectionHandle);
        assertEquals(PicoP_RC.eSUCCESS, ret);

        ret = ALC_Api.PicoP_ALC_OpenConnection(connectionHandle, PicoP_ConnectionTypeE.eUSB, connectionInfo);
        assertEquals(PicoP_RC.eSUCCESS, ret);

        ///////////////////////////


        for(int i = 0; i<100; i++) {
        ret = ALC_Api.PicoP_ALC_SendDebugUSBCommand(connectionHandle, "c0000210161d1d0b2c00000000000004d0020099990000000000005f");
        assertEquals(PicoP_RC.eSUCCESS, ret);

        }
    }
}

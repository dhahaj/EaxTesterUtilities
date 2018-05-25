package com.tester.utilities;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.nio.file.Path;

class Test {

	@org.junit.jupiter.api.Test
	void test() {
		
		ExecUpProgram e = new ExecUpProgram(new File("C:\\tester\\Resources\\firmware\\EAX300.hex").toPath(), "DMH");
		try {
			e.runUpProgrammer();
		} catch (ProgrammingException e1) {
			e1.printStackTrace();
		}
	}

	public void TestProgramming(Path p, String u) {
		ExecUpProgram execUpProgram = new ExecUpProgram(p, u);
		System.out.println(p.toString());
		assertNotNull(execUpProgram);
		assertNotNull(execUpProgram.getFirmwareFile());
		assertNotNull(execUpProgram.getUser());
		try {
			assertEquals(execUpProgram.runUpProgrammer(), 0);
		} catch (ProgrammingException e) {
			fail("Exception Caught");
		}
		
	}

}

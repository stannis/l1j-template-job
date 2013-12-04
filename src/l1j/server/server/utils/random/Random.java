/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.server.server.utils.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class Random {
	static //private SecureRandom secureRandom1;
	//private secureRandom1 = SecureRandom.getInstance("SHA1PRNG");
	SecureRandom random = new SecureRandom();
	public static int nextInt(int n) {
    	//final int offset = 123456;  // offset为固定值，避免被猜到种子来源（和密码学中的加salt有点类似）
	    //long seed = System.currentTimeMillis() + offset;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //byte bytes[] = new byte[3];
	    //random.nextBytes(bytes);
	    //byte seed[] = random.generateSeed(3);
		//random.setSeed(seed);
	    //System.out.println(seed);
	   // System.out.println(random.nextInt(n));
	    return random.nextInt(n); 
		//return 0;
    }

}
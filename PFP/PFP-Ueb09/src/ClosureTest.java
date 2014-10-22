

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class ClosureTest {

	@Test
	public void NoBackEdge(){
		int[][] reference = {{1,2,3,4,5},{2,3,4,5},{3,4,5},{4,5},{5},{}};

		int[][] adj = new int[][] {
				{  -1,  1, -1, -1, -1, -1},
				{  -1, -1,  1, -1, -1, -1},
				{  -1, -1, -1,  1, -1, -1},
				{  -1, -1, -1, -1,  1, -1},
				{  -1, -1, -1, -1, -1,  1},
				{  -1, -1, -1, -1, -1, -1}};
		Closure test = new Closure();
		LinkedList<Integer>[] res = test.closure(adj);
		for (int i = 0; i< adj.length;i++){
			System.out.println((String.valueOf((char)(65+i)))+":");
			for (Integer j : res[i]){
				System.out.print((String.valueOf((char)(65+j)))+", ");
			}
			System.out.println();
		}

		for (int i = 0; i < adj.length; i++){
			if (reference[i]==null){
				assertTrue(res[i].size()==0);
			} else {
				assertTrue(res[i].size()==reference[i].length);
				for (int j = 0; j < reference[i].length; j++){
					assertTrue(res[i].contains(reference[i][j]));
				}
			}

		}
	}

	@Test
	public void BackEdge(){
		int[][] reference = {{0,1,2,3,4,5},{0,1,2,3,4,5},{3,4,5},{4,5},{5},{}};
		int[][] adj = new int[][] {
				{  -1,  1, -1, -1, -1, -1},
				{  1, -1,  1, -1, -1, -1},
				{  -1, -1, -1,  1, -1, -1},
				{  -1, -1, -1, -1,  1, -1},
				{  -1, -1, -1, -1, -1,  1},
				{  -1, -1, -1, -1, -1, -1}};
		Closure test = new Closure();
		LinkedList<Integer>[] res = test.closure(adj);
		for (int i = 0; i< adj.length;i++){
			System.out.println((String.valueOf((char)(65+i)))+":");
			for (Integer j : res[i]){
				System.out.print((String.valueOf((char)(65+j)))+", ");
			}
			System.out.println();
		}

		for (int i = 0; i < adj.length; i++){
			if (reference[i]==null){
				assertTrue(res[i].size()==0);
			} else {
				assertTrue(res[i].size()==reference[i].length);
				for (int j = 0; j < reference[i].length; j++){
					assertTrue(res[i].contains(reference[i][j]));
				}
			}

		}
	}


}

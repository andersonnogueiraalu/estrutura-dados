package com.estrutura.dados.leetcode;

import java.util.Arrays;

// exemplo:
// numeros = [2, 7, 11, 15], alvo = 9
// resposta: [0, 1] porque numeros[0] + numeros[1] = 2 + 7 = 9
class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }
}

class Main {
    public static void main(String[] args) {
        TwoSum ts = new TwoSum();
        int[] res = ts.twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println(Arrays.toString(res));
    }
}




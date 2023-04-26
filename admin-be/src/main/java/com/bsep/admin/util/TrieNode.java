package com.bsep.admin.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private String content;
    private boolean isWord;


    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
        content = "";
    }


}

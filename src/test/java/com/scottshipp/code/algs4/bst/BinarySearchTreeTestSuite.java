package com.scottshipp.code.algs4.bst;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("Binary Search Tree Test Suite")
@SelectClasses( { BSTTraversalTest.class, BSTFindTest.class } )
public class BinarySearchTreeTestSuite {
    // test suite definition -- empty on purpose
}

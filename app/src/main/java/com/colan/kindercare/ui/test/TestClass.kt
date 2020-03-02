package com.colan.kindercare.ui.test

class TestClass:InterfaceA,InterfaceB {

    override fun print(a:String) {
        System.out.println(""+a)
    }
    fun main(args : Array<String>) {
         var a:InterfaceA=TestClass()
        a.print("A")
        var b:InterfaceB=TestClass()
        b.print("B")
    }
}
package com.nohana.projetoiot.controller

import com.nohana.projetoiot.model.Animal
import com.nohana.projetoiot.R
import com.nohana.projetoiot.model.Device

class AparelhoController {

    var devices: MutableList<Device> = mutableListOf()

    constructor() {
        initDevices()
    }

    fun initDevices() {

        var a1 = Animal("Cachorro", R.drawable.cachorro)

        var d1 = Device("Aparelho 1", a1, false)

        devices.add(d1)
    }
}
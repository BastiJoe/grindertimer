//
//  Constants.swift
//  Grinder Timer
//
//  Created by Hamza Farooq on 04/04/2020.
//  Copyright © 2020 Hamza Farooq. All rights reserved.
//

import UIKit

class Constants {
    static var singleShot:    Int = 7100
    static var doubleShot:    Int = 10500
    static var pause:         Int = 4000
    static var power:         Int = 50
    static var wait:          Int = 1000
    static var shotMode:     Bool = true
    static var isConnected:  Bool = false
    static var isSocketReady:Bool = false
    static var showPower:    Bool = false
    static var showAdvanced: Bool = false
    static var password:   String = "fwupdate57"
}

extension String {
    func withoutWhitespace() -> String {
        return self.replacingOccurrences(of: "\n", with: "")
            .replacingOccurrences(of: "\r", with: "")
            .replacingOccurrences(of: "\0", with: "")
    }
}

struct Colors {
    static let darkCyan = UIColor(red: 0, green: 150/255, blue: 136/255, alpha: 1)
}

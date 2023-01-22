//
//  Firmware.swift
//  Grinder Timer
//
//  Created by Hamza Farooq on 13/04/2020.
//  Copyright © 2020 Hamza Farooq. All rights reserved.
//

import UIKit
import WebKit

class Firmware: UIViewController {

    @IBOutlet weak var webView: WKWebView!
    @IBOutlet weak var verificationView: UIView!
    @IBOutlet weak var txtPassword: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        webView.isHidden = true
        txtPassword.isSecureTextEntry = true
    }
    @IBAction func btnVerifyTapped(_ sender: UIButton) {
        let password = txtPassword.text
        if password == Constants.password {
            showUpdateMenu()
        } else {
            txtPassword.placeholder = "Invalid Password"
        }
    }

    private func showUpdateMenu(){
        verificationView.isHidden = true;
        webView.isHidden = false
        if let url = URL(string: "http://192.168.4.1:81/update") {
            let request = URLRequest(url: url)
            webView.load(request)
        }
    }
}

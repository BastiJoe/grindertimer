//
//  ViewController.swift
//  Grinder Timer
//
//  Created by Hamza Farooq on 04/04/2020.
//  Copyright © 2020 Hamza Farooq. All rights reserved.
//

import UIKit
import NetworkExtension

enum messageType: String, Decodable {
    case normal, Chetan
}

enum messageForType: String, Decodable {
    case ping, connected
}

struct checkMessageForType: Decodable{
    let messageFor: messageForType
}

struct checkPingMessageType: Decodable {
    let Message: messageType
}

struct baseVariable: Decodable {
    let single:     Int
    let double:     Int
    let power:      Int
    let waiting:    Int
    let pause:      Int
    let shot:       String
    let messageFor: messageForType
}

struct pingVariables: Decodable {
    let Status:     Int
    let Progress:   Int
    let Maxtime:    Int
    let Power:      Int
    let Message:    messageType
    let messageFor: messageForType
}

class ViewController: UIViewController {
    
    @IBOutlet weak var imgStatus: UIImageView!
    @IBOutlet weak var lblStatus: UILabel!
    @IBOutlet weak var progressBar: UIProgressView!
    
    @IBOutlet weak var modeControl: UISegmentedControl!
    
    @IBOutlet weak var lblDouble: UILabel!
    @IBOutlet weak var imgDouble: UIImageView!
    
    @IBOutlet weak var lblDoubleValue: UILabel!
    @IBOutlet weak var btnDoubleSubtract: UIButton!
    @IBOutlet weak var btnDoubleAdd: UIButton!
    
    @IBOutlet weak var lblSingleValue: UILabel!
    @IBOutlet weak var btnSingleSubtract: UIButton!
    @IBOutlet weak var btnSingleAdd: UIButton!
    @IBOutlet weak var lblPower: UILabel!
    @IBOutlet weak var btnConnect: UIButton!
    
    var inputStream: InputStream!
    var outputStream: OutputStream!
    
    let maxReadLength         = 1024
    let PING_MSG              = "SOCKET_PING"
    let CONNECTED_MSG         = "SOCKET_CONNECTED"
    let DISCONNECTED_MSG      = "SOCKET_DISCONNECTED"
    
    let hostname              = "192.168.4.1"
    let port: UInt32          = 80
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        setupNetworkCommunication()
    }
    
    @IBAction func modeChange(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 0 {
            Constants.shotMode = false
        } else {
            Constants.shotMode = true
        }
        changeShotMode(newMode: Constants.shotMode)
    }
    
    @IBAction func btnSingleSubtractPressed(_ sender: UIButton) {
        if Constants.singleShot > 1000 {
            Constants.singleShot -= 100
        }
        let newValue          = Double(Constants.singleShot)/1000
        lblSingleValue.text   = String(newValue)
    }
    
    @IBAction func btnSingleAddPressed(_ sender: UIButton) {
        Constants.singleShot += 100
        let newValue          = Double(Constants.singleShot)/1000
        lblSingleValue.text   = String(newValue)
    }
    
    @IBAction func btnDoubleSubtractPressed(_ sender: UIButton) {
        if Constants.doubleShot > 1000 {
            Constants.doubleShot -= 100
        }
        let newValue          = Double(Constants.doubleShot)/1000
        lblDoubleValue.text   = String(newValue)
    }
    
    @IBAction func btnDoubleAddPressed(_ sender: UIButton) {
        Constants.doubleShot += 100
        let newValue          = Double(Constants.doubleShot)/1000
        lblDoubleValue.text   = String(newValue)
    }
    
    @IBAction func btnConnectTapped(_ sender: UIButton) {
        if !Constants.isConnected {
            setupNetworkCommunication()
        }
    }
    
    private func setupNetworkCommunication() {
        var readStream:  Unmanaged<CFReadStream>?
        var writeStream: Unmanaged<CFWriteStream>?
        
        CFStreamCreatePairWithSocketToHost(kCFAllocatorMalloc,"192.168.4.1" as CFString, port, &readStream, &writeStream)
        
        inputStream  = readStream!.takeRetainedValue()
        outputStream = writeStream!.takeRetainedValue()
        
        inputStream.delegate = self
        
        inputStream.schedule(in: .current, forMode: .common)
        outputStream.schedule(in: .current, forMode: .common)
        
        inputStream.open()
        outputStream.open()
    }
    
    private func hideDoubleShot(){
        lblDouble.isHidden         = true
        lblDoubleValue.isHidden    = true
        imgDouble.isHidden         = true
        btnDoubleAdd.isHidden      = true
        btnDoubleSubtract.isHidden = true
    }
    
    private func showDoubleShot(){
        lblDouble.isHidden         = false
        lblDoubleValue.isHidden    = false
        imgDouble.isHidden         = false
        btnDoubleAdd.isHidden      = false
        btnDoubleSubtract.isHidden = false
    }
    
    private func updateValues() {
        self.lblSingleValue.text = String(Double(Constants.singleShot)/1000)
        self.lblDoubleValue.text = String(Double(Constants.doubleShot)/1000)
        changeShotMode(newMode: Constants.shotMode)
        if Constants.shotMode {
            modeControl.selectedSegmentIndex = 1
        } else {
            modeControl.selectedSegmentIndex = 0
        }
    }
    
    private func changeShotMode(newMode: Bool) {
        if newMode {
            showDoubleShot()
        } else {
            hideDoubleShot()
        }
    }
    
    private func deviceConnected(){
        self.imgStatus.image      = UIImage(named: "wi-fi-connected")
        self.btnConnect.isEnabled = false
        self.lblStatus.textColor  = Colors.darkCyan
        self.lblStatus.text       = "Connected"
        Constants.isConnected     = true
        self.btnConnect.setTitle("CONNECTED", for: .normal)
    }
    
    private func deviceDisconnected(){
        self.imgStatus.image      = UIImage(named: "wi-fi-slash")
        self.btnConnect.isEnabled = true
        self.lblStatus.textColor  = .red
        self.lblStatus.text       = "Disconnected"
        Constants.isConnected     = false
        self.btnConnect.setTitle("CONNECT TO GRINDER", for: .normal)
        inputStream.close()
        outputStream.close()
        
    }
}

extension ViewController: StreamDelegate {
    
    func stream(_ aStream: Stream, handle eventCode: Stream.Event) {
        switch eventCode {
        case .hasBytesAvailable:
            readAvailableBytes(stream: aStream as! InputStream)
            print("new message received.")
        case .endEncountered:
            deviceDisconnected()
            print("endEncountered")
        case .errorOccurred:
            deviceDisconnected()
            print("error occurred")
        case .hasSpaceAvailable:
            print("has space available")
        default:
            print("some other event...")
        }
    }
    
    private func readAvailableBytes(stream: InputStream) {
        var buffer = [UInt8](repeating: 0, count: maxReadLength)
        while inputStream.hasBytesAvailable {
            let numberOfBytesRead = inputStream.read(&buffer, maxLength: maxReadLength)
            if numberOfBytesRead < 0 {
                break
            }
            if let packet = String(bytes: buffer[..<numberOfBytesRead], encoding: .ascii) {
                if packet.withoutWhitespace() == CONNECTED_MSG {
                    print(packet)
                    deviceConnected()
                    return
                }
                if packet.withoutWhitespace() == DISCONNECTED_MSG {
                    deviceDisconnected()
                    return
                }
                if packet.withoutWhitespace() == PING_MSG {
                    Constants.isSocketReady = true
                    return
                }
                if Constants.isConnected && Constants.isSocketReady {
                    processPacket(packet: packet)
                    return
                }
            }
        }
    }
    
    private func processPacket(packet: String) {
        do{
            if !Constants.showPower {
                lblPower.isHidden = true
            } else {
                lblPower.isHidden = false
            }
            guard let data = packet.data(using: String.Encoding.utf8) else { return }
            let checkMessageFor: checkMessageForType = try JSONDecoder().decode(checkMessageForType.self, from: data)
            if checkMessageFor.messageFor == messageForType.connected {
                let base: baseVariable = try JSONDecoder().decode(baseVariable.self, from: data)
                Constants.singleShot = base.single
                Constants.doubleShot = base.double
                Constants.power      = base.power
                Constants.wait       = base.waiting
                Constants.pause      = base.pause
                if base.shot == "true" {
                    Constants.shotMode = true
                } else {
                    Constants.shotMode = false
                }
                self.updateValues()
            }
            else if checkMessageFor.messageFor == messageForType.ping {
                let checkPingMessage: checkPingMessageType = try JSONDecoder().decode(checkPingMessageType.self, from: data)
                if checkPingMessage.Message == messageType.normal {
                    let status: pingVariables = try JSONDecoder().decode(pingVariables.self, from: data)
                    lblPower.text = "Power: " + String(status.Power) + "w"
                    if status.Status == 0 {
                        self.progressBar.isHidden = true
                        self.lblStatus.text       = "Ready"
                        self.lblStatus.textColor  = Colors.darkCyan
                        self.imgStatus.image      = UIImage(named: "logo-ready")
                    }
                    else if status.Status == 1 {
                        self.progressBar.isHidden = false
                        self.lblStatus.text       = "Running Single"
                        self.lblStatus.textColor  = Colors.darkCyan
                        self.progressBar.progress = Float(status.Progress)/100
                        self.imgStatus.image      = UIImage(named: "logo-single-status")
                    }
                    else if status.Status == 2 {
                        self.progressBar.isHidden = true
                        self.lblStatus.text       = "Paused"
                        self.lblStatus.textColor  = Colors.darkCyan
                        self.imgStatus.image      = UIImage(named: "logo-paused")
                    }
                    else if status.Status == 3 {
                        self.progressBar.isHidden = true
                        self.lblStatus.text       = "Watchdog"
                        self.lblStatus.textColor  = Colors.darkCyan
                        self.imgStatus.image      = UIImage(named: "logo-watchdog")
                    }
                    else if status.Status == 4 {
                        self.progressBar.isHidden = false
                        self.lblStatus.text       = "Running Double"
                        self.lblStatus.textColor  = Colors.darkCyan
                        self.progressBar.progress = Float(status.Progress)/100
                        self.imgStatus.image      = UIImage(named: "logo-double-status")
                    }
                    else if status.Status == 5 {
                        self.progressBar.isHidden = true
                        self.lblStatus.text       = "Paused"
                        self.lblStatus.textColor  = Colors.darkCyan
                        self.imgStatus.image      = UIImage(named: "logo-paused")
                    }
                } else if checkPingMessage.Message == messageType.Chetan {
                    let postData = "{\"singleshot\":\"\(Constants.singleShot)\",\"doubleshot\":\"\(Constants.doubleShot)\",\"pause\":\"\(Constants.pause)\",\"wait\":\(Constants.wait),\"trash\":\(Constants.power),\"mode\":\(Constants.shotMode)}\n".data(using: .utf8)!
                    do{
                        _ = postData.withUnsafeBytes {
                            guard let pointer = $0.baseAddress?.assumingMemoryBound(to: UInt8.self) else {
                                print("Error posting Data")
                                return
                            }
                            outputStream.write(pointer, maxLength: postData.count)
                        }
                    }
                }
            }
        } catch {
            //                print ("Packet: \(packet) not JSON")
        }
    }
}

//
//  Settings.swift
//  Grinder Timer
//
//  Created by Hamza Farooq on 04/04/2020.
//  Copyright © 2020 Hamza Farooq. All rights reserved.
//

import UIKit

class Settings: UITableViewController {
    
    @IBOutlet weak var lblPower: UILabel!
    @IBOutlet weak var lblWait:  UILabel!
    @IBOutlet weak var lblPause: UILabel!
    
    @IBOutlet weak var switchShowPower: UISwitch!
    @IBOutlet weak var switchAdvancedSettings: UISwitch!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        lblPower.text = String(Constants.power) + " w"
        lblWait.text = String(Constants.wait) + " ms"
        lblPause.text = String(Double(Constants.pause)/1000)
        switchShowPower.isOn = Constants.showPower
        switchAdvancedSettings.isOn = Constants.showAdvanced
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
         
        let advancedRow = IndexPath(row: 3, section: 0)
        Constants.showAdvanced = switchAdvancedSettings.isOn
        
        if indexPath == advancedRow  {
            if !Constants.showAdvanced {
                return 0
            }
            return 132
        }
        return 44
    }
    
    @IBAction func btnPowerMinus(_ sender: UIButton) {
        if Constants.power > 50{
            Constants.power -= 5
        }
        lblPower.text = String(Constants.power) + " w"
    }
    @IBAction func btnPowerPlus(_ sender: UIButton) {
        Constants.power += 5
        lblPower.text = String(Constants.power) + " w"
    }
    @IBAction func btnWaitMinus(_ sender: UIButton) {
        if Constants.wait > 1000{
            Constants.wait -= 50
        }
        lblWait.text = String(Constants.wait) + " ms"
    }
    @IBAction func btnWaitPlus(_ sender: UIButton) {
        Constants.wait += 50
        lblWait.text = String(Constants.wait) + " ms"
    }
    @IBAction func btnPauseMinus(_ sender: UIButton) {
        if Constants.pause > 4000 {
            Constants.pause -= 100
        }
        let newValue = Double(Constants.pause)/1000
        lblPause.text = String(newValue)
    }
    @IBAction func btnPausePlus(_ sender: UIButton) {
        Constants.pause += 100
        let newValue = Double(Constants.pause)/1000
        lblPause.text = String(newValue)
    }
    @IBAction func btnTutorialTapped(_ sender: UIButton) {
        if let url = URL(string: "http://grindertimer.com/tutorial"){
            UIApplication.shared.open(url)
        }
    }
    
    @IBAction func btnContactTapped(_ sender: UIButton) {
        if let url = URL(string: "mailto:info@grindertimer.com"){
            UIApplication.shared.open(url)
        }
    }
    
    @IBAction func switchAdvancedSettingsValueChanged(_ sender: UISwitch) {
        let indexPath = IndexPath(row: 3, section: 0)
        tableView.reloadRows(at: [indexPath], with: UITableView.RowAnimation.bottom)
    }
    
    @IBAction func switchPowerValueChanged(_ sender: UISwitch) {
        Constants.showPower = switchShowPower.isOn
    }
}

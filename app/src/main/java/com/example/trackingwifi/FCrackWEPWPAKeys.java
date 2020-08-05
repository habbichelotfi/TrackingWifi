package com.example.trackingwifi;

// --------------------------------------------------------------------------------
// Name: FCrackWEPWPAKeys
// Abstract: Allows you to break WEP and WPA keys
// --------------------------------------------------------------------------------

// Includes
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import javax.swing.*;
/*
public class FCrackWEPWPAKeys extends CAircrackWindow {
    protected final static long serialVersionUID = 1L;

    // Controls
    private CTextBox m_txtIVSCAPFileLocation = null;
    private JButton m_btnIVSCAPFileLocation = null;

    // Common Functions
    private JCheckBox m_chkForceAttackMode = null; // -a
    private CComboBox m_cboForceAttackMode = null;
    private JCheckBox m_chkTargetNetworkESSID = null; // -e
    private CTextBox m_txtTargetNetworkESSID = null;
    private JButton m_btnTargetNetworkESSID = null;
    private JCheckBox m_chkTargetNetworkBSSID = null; // -b
    private CTextBox m_txtTargetNetworkBSSID = null;
    private JButton m_btnTargetNetworkBSSID = null;
    private JCheckBox m_chkLimitCPUCores = null; // -p
    private CComboBox m_cboLimitCPUCores = null;
    private JCheckBox m_chkHideStatusInformation = null; // -q
    private JCheckBox m_chkWriteKeyToFile = null; // -l
    private CTextBox m_txtWriteKeyToFile = null;
    private JButton m_btnWriteKeyToFile = null;

    // Static WEP Functions
    private JCheckBox m_chkSearchAlphaNumericCharacters = null; // -c
    private JCheckBox m_chkSearchBinaryCodedDecimalCharacters = null; // -t
    private JCheckBox m_chkWEPDecloakMode = null; // -D
    private JCheckBox m_chkOnlyOneAttemptAtCrack = null; // -1
    private JCheckBox m_chkShowASCIIKey = null; // -s
    private JCheckBox m_chkSpecifyKeyMask = null; // -d
    private CTextBox m_txtSpecifyKeyMask = null;
    private JCheckBox m_chkIVsFromMACAddress = null; // -m
    private CTextBox m_txtIVsFromMACAddress = null;
    private JCheckBox m_chkSpecifyKeyLength = null; // -n
    private CTextBox m_txtSpecifyKeyLength = null;
    private JCheckBox m_chkSpecifyBruteForceLevel = null; // -f
    private CTextBox m_txtSpecifyBruteForceLevel = null;
    private JCheckBox m_chkSpecifyAttackMethod = null; // -z = PTW, -K = KoreK
    private CComboBox m_cboSpecifyAttackMethod = null;
    private JCheckBox m_chkSpecifyMaxIVsUsed = null; // -M
    private CTextBox m_txtSpecifyMaxIVsUsed = null;

    // WPA-PSK Functions
    private JCheckBox m_chkSpecifyDictionaryFile = null; // -w
    private CTextBox m_txtSpecifyDictionaryFile = null;
    private JButton m_btnSpecifyDictionaryFile = null;
    private JCheckBox m_chkSpecifyDatabaseFile = null;
    private CTextBox m_txtSpecifyDatabaseFile = null;
    private JButton m_btnSpecifyDatabaseFile = null;

    private JButton m_btnStart = null;

    // --------------------------------------------------------------------------------
    // Name: FCrackWEPWPAKeys
    // Abstract: Class constructor
    // --------------------------------------------------------------------------------
    public FCrackWEPWPAKeys( )
    {
        super("Crack WEP/WPA Keys", 450, 600, false, false, "");
        try
        {
            AddControls( );
            PopulateAttackModes( );
            PopulateCPUCores( );
            PopulateAttackMethods( );
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: AddControls
    // Abstract: Adds controls to the window
    // --------------------------------------------------------------------------------
    private void AddControls( )
    {
        try
        {
            // Spring layout for frame
            SpringLayout splFrame = new SpringLayout( );
            Container conControlArea = this.getContentPane( );
            conControlArea.setLayout(  splFrame );

            CUtilities.AddLabel(conControlArea, "IVS/CAP File Location:", 16, 5);
            m_txtIVSCAPFileLocation = CUtilities.AddTextBox(conControlArea, null, "", 15, 100, 15, 165);
            m_btnIVSCAPFileLocation = CUtilities.AddButton(conControlArea, this, "...", 15, 340, 18, 50);

            CUtilities.AddLabel(conControlArea, "Common Functions:", 45, 5);
            m_chkForceAttackMode = CUtilities.AddCheckBox(conControlArea, this, "Force Attack Mode:", 60, 5);
            m_cboForceAttackMode = CUtilities.AddComboBox(conControlArea, null, 63, 170, 18, 125);
            m_chkTargetNetworkESSID = CUtilities.AddCheckBox(conControlArea, this, "Target Network - ESSID:", 83, 5);
            m_txtTargetNetworkESSID = CUtilities.AddTextBox(conControlArea, null, "", 15, 100, 85, 200);
            m_btnTargetNetworkESSID = CUtilities.AddButton(conControlArea, this, "...", 85, 375, 18, 50);
            m_chkTargetNetworkBSSID = CUtilities.AddCheckBox(conControlArea, this, "Target Network - BSSID:", 108, 5);
            m_txtTargetNetworkBSSID = CUtilities.AddTextBox(conControlArea, null, "", 15, 100, 110, 200);
            m_btnTargetNetworkBSSID = CUtilities.AddButton(conControlArea, this, "...", 110, 375, 18, 50);
            m_chkLimitCPUCores = CUtilities.AddCheckBox(conControlArea, this, "Limit CPU Cores:", 133, 5);
            m_cboLimitCPUCores = CUtilities.AddComboBox(conControlArea, null, 135, 150, 18, 50);
            m_chkHideStatusInformation = CUtilities.AddCheckBox(conControlArea, this, "Hide Status Information", 158, 5);
            m_chkWriteKeyToFile = CUtilities.AddCheckBox(conControlArea, this, "Write Key To File:", 183, 5);
            m_txtWriteKeyToFile = CUtilities.AddTextBox(conControlArea, null, "", 15, 100, 185, 155);
            m_btnWriteKeyToFile = CUtilities.AddButton(conControlArea, this, "...", 185, 330, 18, 50);

            CUtilities.AddLabel(conControlArea, "Static WEP Functions:", 225, 5);
            m_chkSearchAlphaNumericCharacters = CUtilities.AddCheckBox(conControlArea, this, "Alpha-numeric password", 243, 5);
            m_chkSearchBinaryCodedDecimalCharacters = CUtilities.AddCheckBox(conControlArea, this, "Decimal password", 243, 225);
            m_chkWEPDecloakMode = CUtilities.AddCheckBox(conControlArea, this, "WEP De-Cloak Mode", 268, 5);
            m_chkOnlyOneAttemptAtCrack = CUtilities.AddCheckBox(conControlArea, this, "Attempt crack once", 268, 225);
            m_chkShowASCIIKey = CUtilities.AddCheckBox(conControlArea, this, "Show ASCII Key on Right", 293, 5);
            m_chkSpecifyKeyMask = CUtilities.AddCheckBox(conControlArea, this, "Specify Key Mask:", 318, 5);
            m_txtSpecifyKeyMask = CUtilities.AddTextBox(conControlArea, null, "", 6, 8, 320, 160);
            m_chkIVsFromMACAddress = CUtilities.AddCheckBox(conControlArea, this, "IVs From MAC Address:", 343, 5);
            m_txtIVsFromMACAddress = CUtilities.AddTextBox(conControlArea, null, "ff:ff:ff:ff:ff:ff", 7, 17, 345, 195);
            m_chkSpecifyKeyLength = CUtilities.AddCheckBox(conControlArea, this, "Specify Key Length:", 368, 5);
            m_txtSpecifyKeyLength = CUtilities.AddTextBox(conControlArea, null, "128", 3, 3, 370, 175);
            m_chkSpecifyBruteForceLevel = CUtilities.AddCheckBox(conControlArea, this, "Brute Force Level:", 393, 5); // -f
            m_txtSpecifyBruteForceLevel = CUtilities.AddTextBox(conControlArea, null, "2", 3, 3, 395, 160);
            m_chkSpecifyAttackMethod = CUtilities.AddCheckBox(conControlArea, this, "Specify Attack Method:", 418, 5); // -z = PTW, -K = KoreK
            m_cboSpecifyAttackMethod = CUtilities.AddComboBox(conControlArea, null, 420, 195, 18, 90);
            m_cboSpecifyAttackMethod.SetSorted( false );
            m_chkSpecifyMaxIVsUsed = CUtilities.AddCheckBox(conControlArea, this, "Max IVs Used:", 443, 5);
            m_txtSpecifyMaxIVsUsed = CUtilities.AddTextBox(conControlArea, null, "", 11, 15, 445, 130);

            CUtilities.AddLabel(conControlArea, "WPA-PSK Cracking Options:", 485, 5);
            m_chkSpecifyDictionaryFile = CUtilities.AddCheckBox(conControlArea, this, "Dictionary File:", 503, 5);
            m_txtSpecifyDictionaryFile = CUtilities.AddTextBox(conControlArea, null, "", 15, 100, 505, 140);
            m_btnSpecifyDictionaryFile = CUtilities.AddButton(conControlArea, this, "...", 505, 315, 18, 50);
            m_chkSpecifyDatabaseFile = CUtilities.AddCheckBox(conControlArea, this, "Database File:", 523, 5);
            m_txtSpecifyDatabaseFile = CUtilities.AddTextBox(conControlArea, null, "", 15, 100, 525, 140);
            m_btnSpecifyDatabaseFile = CUtilities.AddButton(conControlArea, this, "...", 525, 315, 18, 50);

            m_btnStart = CUtilities.AddButton(conControlArea, this, "Start", 545, 175, 18, 100);

        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: PopulateAttackModes
    // Abstract: Populates the possible crack attack modes
    // --------------------------------------------------------------------------------
    private void PopulateAttackModes( )
    {
        try
        {
            CAircrackNGProcess.udtAttackModeType[] audtAttackModes = CAircrackNGProcess.udtAttackModeType.values();
            for (int intIndex = 0; intIndex < audtAttackModes.length; intIndex++)
                m_cboForceAttackMode.AddItemToList(audtAttackModes[intIndex].toString(), intIndex);

            m_cboForceAttackMode.SetSelectedIndex( 0 );
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: PopulateCPUCores
    // Abstract: Populates number of CPU cores available for cracking
    // --------------------------------------------------------------------------------
    private void PopulateCPUCores( )
    {
        try
        {
            CProcess clsLsCpu = new CProcess(new String[] {"lscpu"}, true, true, false);
            BufferedReader brOutput = new BufferedReader( clsLsCpu.GetOutput( ) );
            String strBuffer = brOutput.readLine( );
            int intCoreCount = 1;

            while ( strBuffer != null )
            {
                if ( strBuffer.indexOf("CPU(s):") >= 0 )
                {
                    strBuffer = strBuffer.substring( 7 ).trim( );
                    intCoreCount = Integer.parseInt( strBuffer );
                    break;
                }
                strBuffer = brOutput.readLine( );
            }

            clsLsCpu.CloseProcess();

            for ( int intCoreIndex = 1; intCoreIndex <= intCoreCount; intCoreIndex += 1 )
            {
                m_cboLimitCPUCores.AddItemToList(String.valueOf(intCoreIndex), intCoreIndex);
            }
            m_cboLimitCPUCores.SetSelectedIndex( 0 );

        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: PopulateAttackMethods
    // Abstract: Populates the different attack methods
    // --------------------------------------------------------------------------------
    private void PopulateAttackMethods( )
    {
        try
        {
            CAircrackNGProcess.udtAttackMethodType[] audtAttackMethods = CAircrackNGProcess.udtAttackMethodType.values();
            for (int intIndex = 0; intIndex < audtAttackMethods.length; intIndex++)
                m_cboSpecifyAttackMethod.AddItemToList(audtAttackMethods[intIndex].toString(), intIndex);

            m_cboSpecifyAttackMethod.SetSelectedIndex( 0 );
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: actionPerformed
    // Abstract: Event handler for button and checkbox clicks
    // --------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent aeSource)
    {
        try
        {
            if ( aeSource.getSource( ) == m_btnIVSCAPFileLocation ) 		CAircrackUtilities.DisplayFileChooser(m_txtIVSCAPFileLocation, this);
            else if ( aeSource.getSource( ) == m_btnWriteKeyToFile )		CAircrackUtilities.DisplayFileChooser(m_txtWriteKeyToFile, this);
            else if ( aeSource.getSource( ) == m_btnSpecifyDictionaryFile ) CAircrackUtilities.DisplayFileChooser(m_txtSpecifyDictionaryFile, this);
            else if ( aeSource.getSource( ) == m_btnSpecifyDatabaseFile )	CAircrackUtilities.DisplayFileChooser(m_txtSpecifyDatabaseFile, this);
            else if ( aeSource.getSource( ) == m_btnTargetNetworkESSID ) 	btnTargetNetworkESSID_Click( );
            else if ( aeSource.getSource( ) == m_btnTargetNetworkBSSID ) 	btnTargetNetworkBSSID_Click( );
            else if ( aeSource.getSource( ) == m_btnStart )					btnStart_Click( );
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: btnTargetNetworkESSID_Click
    // Abstract: Displays the select network dialog for specifying a network ESSID
    // --------------------------------------------------------------------------------
    private void btnTargetNetworkESSID_Click( )
    {
        try
        {
            DSelectNetwork dlgSelectNetwork = new DSelectNetwork( );
            dlgSelectNetwork.SetReturnType(DSelectNetwork.udtSelectNetworkReturnType.RETURN_TYPE_NETWORK_ESSID);
            dlgSelectNetwork.setVisible( true );
            m_txtTargetNetworkESSID.setText(dlgSelectNetwork.GetSelectedNetwork( ));
            requestFocus( );
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: btnTargetNetworkBSSID_Click
    // Abstract: Displays the select network dialog for specifying a network BSSID
    // --------------------------------------------------------------------------------
    private void btnTargetNetworkBSSID_Click( )
    {
        try
        {
            DSelectNetwork dlgSelectNetwork = new DSelectNetwork( );
            dlgSelectNetwork.SetReturnType(DSelectNetwork.udtSelectNetworkReturnType.RETURN_TYPE_NETWORK_BSSID);
            dlgSelectNetwork.setVisible( true );
            m_txtTargetNetworkBSSID.setText(dlgSelectNetwork.GetSelectedNetwork( ));
            requestFocus( );
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: btnStart_Click
    // Abstract: Starts the network key cracking process
    // --------------------------------------------------------------------------------
    private void btnStart_Click( )
    {
        try
        {
            CAircrackNGProcess clsAircrackNG = new CAircrackNGProcess();

            // Common Functions
            AppendCommonFunctions(clsAircrackNG);

            // Static WEP Functions
            AppendStaticWEPFunctions(clsAircrackNG);

            // WPA-PSK Cracking Options
            if ( m_chkSpecifyDictionaryFile.isSelected( ) == true )
                clsAircrackNG.SetDictionaryFile(m_txtSpecifyDictionaryFile.getText( ));
            if ( m_chkSpecifyDatabaseFile.isSelected( ) == true )
                clsAircrackNG.SetDatabaseFile(m_txtSpecifyDatabaseFile.getText( ));

            clsAircrackNG.SetIVSCapFile(m_txtIVSCAPFileLocation.getText());
            clsAircrackNG.Crack();

            DProgramOutput dlgCrackWEPWPAKeysResults = new DProgramOutput("Crack WEP/WPA Keys - Output", clsAircrackNG, true);
            dlgCrackWEPWPAKeysResults.setVisible( true );
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: AppendCommonFunctions
    // Abstract: Appends the common options to the command
    // --------------------------------------------------------------------------------
    private void AppendCommonFunctions(CAircrackNGProcess clsAircrackNG)
    {
        try
        {
            if ( m_chkForceAttackMode.isSelected( ) == true )
            {
                CAircrackNGProcess.udtAttackModeType[] audtAttackModes = CAircrackNGProcess.udtAttackModeType.values();
                for (int intIndex = 0; intIndex < audtAttackModes.length; intIndex++)
                    if (audtAttackModes[intIndex].toString().equals(m_cboForceAttackMode.GetSelectedItemName()))
                        clsAircrackNG.SetAttackMode(audtAttackModes[intIndex]);
            }
            if ( m_chkTargetNetworkESSID.isSelected( ) == true )
                clsAircrackNG.SetNetworkESSID(m_txtTargetNetworkESSID.getText( ));
            if ( m_chkTargetNetworkBSSID.isSelected( ) == true )
                clsAircrackNG.SetNetworkBSSID(m_txtTargetNetworkBSSID.getText( ));
            if ( m_chkLimitCPUCores.isSelected( ) == true )
                clsAircrackNG.SetCPUCores(m_cboLimitCPUCores.GetSelectedItemValue());
            if ( m_chkHideStatusInformation.isSelected( ) == true )
                clsAircrackNG.HideStatusInformation();
            if ( m_chkWriteKeyToFile.isSelected( ) == true )
                clsAircrackNG.SetKeyFile(m_txtWriteKeyToFile.getText( ));
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: AppendStaticWEPFunctions
    // Abstract: Appends the static WEP options to the command
    // --------------------------------------------------------------------------------
    private void AppendStaticWEPFunctions(CAircrackNGProcess clsAircrackNG)
    {
        try
        {
            if ( m_chkSearchAlphaNumericCharacters.isSelected( ) == true )
                clsAircrackNG.SearchAlphaNumericCharacters();
            if ( m_chkSearchBinaryCodedDecimalCharacters.isSelected( ) == true )
                clsAircrackNG.SearchBinaryCodedDecimalCharacters();
            if ( m_chkWEPDecloakMode.isSelected( ) == true )
                clsAircrackNG.UseWEPDecloakMode();
            if ( m_chkOnlyOneAttemptAtCrack.isSelected( ) == true )
                clsAircrackNG.SingleCrackAttempt();
            if ( m_chkShowASCIIKey.isSelected( ) == true )
                clsAircrackNG.ShowASCIIKey();
            if ( m_chkSpecifyKeyMask.isSelected( ) == true )
                clsAircrackNG.SetKeyMask(m_txtSpecifyKeyMask.getText( ));
            if ( m_chkIVsFromMACAddress.isSelected( ) == true )
                clsAircrackNG.SetIVsFromMAC(m_txtIVsFromMACAddress.getText( ));
            if ( m_chkSpecifyKeyLength.isSelected( ) == true )
                clsAircrackNG.SetKeyLength(m_txtSpecifyKeyLength.getText( ));
            if ( m_chkSpecifyBruteForceLevel.isSelected( ) == true )
                clsAircrackNG.SetBruteForceLevel(m_txtSpecifyBruteForceLevel.getText( ));
            if ( m_chkSpecifyAttackMethod.isSelected( ) == true )
            {
                CAircrackNGProcess.udtAttackMethodType[] audtAttackMethods = CAircrackNGProcess.udtAttackMethodType.values();
                for (int intIndex = 0; intIndex < audtAttackMethods.length; intIndex++)
                    if (audtAttackMethods[intIndex].toString().equals(m_cboSpecifyAttackMethod.GetSelectedItemName()))
                        clsAircrackNG.SetAttackMethod(audtAttackMethods[intIndex]);
            }
            if ( m_chkSpecifyMaxIVsUsed.isSelected( ) == true )
                clsAircrackNG.SetMaxIVsUsed(m_txtSpecifyMaxIVsUsed.getText( ));
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

    // --------------------------------------------------------------------------------
    // Name: SetNetworkBSSID
    // Abstract: Sets the network BSSID from another outside form. Used by Discover
    //			 Networks.
    // --------------------------------------------------------------------------------
    public void SetNetworkBSSID(String strNetworkBSSID)
    {
        try
        {
            m_txtTargetNetworkBSSID.setText(strNetworkBSSID);
            m_chkTargetNetworkBSSID.setSelected( true );
        }
        catch (Exception excError)
        {
            CUtilities.WriteLog(excError);
        }
    }

}*/

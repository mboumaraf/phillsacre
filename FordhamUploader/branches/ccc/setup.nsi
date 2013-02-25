; Installer for Phripp
; Written by Phill Sacre - http://phillsacre.me.uk

!include "MUI2.nsh"
!define VERSION "1.0.0.0"

;---------------------------
; Script Variables
;---------------------------

Name "Phripp"
OutFile "target\Phripp Setup.exe"

RequestExecutionLevel admin

; The default installation directory
InstallDir $PROGRAMFILES\Phripp

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\Phripp" "Install_Dir"

;---------------------------
; Pages
;---------------------------

!define MUI_ABORTWARNING

!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
  
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

!insertmacro MUI_LANGUAGE "English"

;---------------------------
; Installer Properties
;---------------------------

VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductName" "Phripp"
VIAddVersionKey /LANG=${LANG_ENGLISH} "FileDescription" "Fordham Sermon Uploader"
VIAddVersionKey /LANG=${LANG_ENGLISH} "CompanyName" "Phill Sacre"
VIAddVersionKey /LANG=${LANG_ENGLISH} "FileVersion" ${VERSION}
VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductVersion" ${VERSION}
VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalCopyright" "Copyright (c) Phill Sacre. All rights reserved."

VIProductVersion ${VERSION}

;---------------------------
; Sections
;---------------------------
Section "Phripp Application" MainSec

	SectionIn RO

	SetOutPath $INSTDIR
	
	File /r "target\assembly\*.*"
	File /r "target\*.one-jar.jar"
	
	CreateDirectory "$APPDATA\Phripp"
	
	WriteRegStr HKLM SOFTWARE\Phripp "Install_Dir" "$INSTDIR"
	
	; Write the uninstall keys for Windows
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Phripp" "DisplayName" "Phripp"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Phripp" "UninstallString" '"$INSTDIR\uninstall.exe"'
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Phripp" "NoModify" 1
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Phripp" "NoRepair" 1
	
	WriteUninstaller "$INSTDIR\uninstall.exe"
	
SectionEnd

Section "Start Menu Shortcut" StartMenu
	CreateDirectory "$SMPROGRAMS\Phripp"
	CreateShortCut "$SMPROGRAMS\Phripp\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
	CreateShortCut "$SMPROGRAMS\Phripp\Phripp.lnk" "$INSTDIR\Phripp.exe" "" "$INSTDIR\Phripp.exe" 0
SectionEnd

Section "Desktop Shortcut" DesktopShortcut
	CreateShortCut "$DESKTOP\Phripp.lnk" "$INSTDIR\Phripp.exe" "" "$INSTDIR\Phripp.exe" 0
SectionEnd

;---------------------------
; Descriptions
;---------------------------

LangString DESC_MainSec ${LANG_ENGLISH} "Install the main Phripp application (required)."
LangString DESC_StartMenu ${LANG_ENGLISH} "Create a shortcut in the start menu."
LangString DESC_DesktopShortcut ${LANG_ENGLISH} "Place a shortcut on the desktop."

;Assign language strings to sections
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${MainSec} $(DESC_MainSec)
!insertmacro MUI_DESCRIPTION_TEXT ${StartMenu} $(DESC_StartMenu)
!insertmacro MUI_DESCRIPTION_TEXT ${DesktopShortcut} $(DESC_DesktopShortcut)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

;---------------------------
; Uninstaller
;---------------------------

Section "Uninstall"

	; Remove registry keys
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Phripp"
	DeleteRegKey HKLM SOFTWARE\Phripp

	; Remove all program files
	RMDir /r $INSTDIR
	
	; Delete AppData folder
	RMDir /r "$APPDATA\Phripp"
	
	; Remove shortcuts, if any
	RMDir /r "$SMPROGRAMS\Phripp"
	Delete "$DESKTOP\Phripp.lnk"

SectionEnd
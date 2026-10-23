param(
    [string]$Title = "Aether Calculator",
    [int]$X = 0,
    [int]$Y = 0,
    [switch]$Capture,
    [string]$Out = ""
)

Add-Type @"
using System;
using System.Runtime.InteropServices;
public class Click {
    [DllImport("user32.dll")] public static extern bool SetProcessDPIAware();
    [DllImport("user32.dll")] public static extern bool SetCursorPos(int x, int y);
    [DllImport("user32.dll")] public static extern void mouse_event(uint f, uint x, uint y, uint d, IntPtr e);
    [DllImport("user32.dll")] public static extern bool SetForegroundWindow(IntPtr h);
    [StructLayout(LayoutKind.Sequential)] public struct RECT { public int Left, Top, Right, Bottom; }
    [DllImport("user32.dll")] public static extern bool GetWindowRect(IntPtr h, out RECT r);
}
"@

# Clicks are sent in physical pixels, so the process has to be DPI aware first.
[Click]::SetProcessDPIAware() | Out-Null

$process = Get-Process | Where-Object { $_.MainWindowTitle -eq $Title } | Select-Object -First 1
if ($process -eq $null) { Write-Output "window not found"; exit 1 }
$handle = $process.MainWindowHandle

[Click]::SetForegroundWindow($handle) | Out-Null
Start-Sleep -Milliseconds 200

$rect = New-Object Click+RECT
[Click]::GetWindowRect($handle, [ref]$rect) | Out-Null

$screenX = $rect.Left + $X
$screenY = $rect.Top + $Y

[Click]::SetCursorPos($screenX, $screenY) | Out-Null
Start-Sleep -Milliseconds 120
[Click]::mouse_event(0x0002, 0, 0, 0, [IntPtr]::Zero)
Start-Sleep -Milliseconds 60
[Click]::mouse_event(0x0004, 0, 0, 0, [IntPtr]::Zero)
Write-Output "clicked $screenX,$screenY (window $X,$Y)"

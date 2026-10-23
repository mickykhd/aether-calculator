param(
    [string]$Title = "Aether Calculator",
    [string]$Out = "D:\Projects\personal\calculator\screenshots\desktop.png"
)

Add-Type -AssemblyName System.Drawing
Add-Type @"
using System;
using System.Runtime.InteropServices;
public class Win {
    [DllImport("user32.dll")] public static extern bool SetProcessDPIAware();
    [DllImport("user32.dll", CharSet = CharSet.Unicode)] public static extern IntPtr FindWindow(string c, string n);
    [DllImport("user32.dll")] public static extern bool GetWindowRect(IntPtr h, out RECT r);
    [DllImport("user32.dll")] public static extern bool SetForegroundWindow(IntPtr h);
    [DllImport("user32.dll")] public static extern bool GetClientRect(IntPtr h, out RECT r);
    [DllImport("user32.dll")] public static extern bool ClientToScreen(IntPtr h, ref POINT p);
    [StructLayout(LayoutKind.Sequential)] public struct RECT { public int Left, Top, Right, Bottom; }
    [StructLayout(LayoutKind.Sequential)] public struct POINT { public int X, Y; }
}
"@

# The window is captured in physical pixels, so the process has to be DPI aware first.
[Win]::SetProcessDPIAware() | Out-Null

$handle = [Win]::FindWindow($null, $Title)
if ($handle -eq [IntPtr]::Zero) {
    # Fall back to the process that owns the window.
    $process = Get-Process | Where-Object { $_.MainWindowTitle -eq $Title } | Select-Object -First 1
    if ($process -eq $null) { Write-Output "window not found"; exit 1 }
    $handle = $process.MainWindowHandle
}
if ($handle -eq [IntPtr]::Zero) { Write-Output "window not found"; exit 1 }

[Win]::SetForegroundWindow($handle) | Out-Null
Start-Sleep -Milliseconds 400

$rect = New-Object Win+RECT
[Win]::GetWindowRect($handle, [ref]$rect) | Out-Null
$width = $rect.Right - $rect.Left
$height = $rect.Bottom - $rect.Top

$bitmap = New-Object System.Drawing.Bitmap $width, $height
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)
$graphics.CopyFromScreen($rect.Left, $rect.Top, 0, 0, $bitmap.Size)
$bitmap.Save($Out, [System.Drawing.Imaging.ImageFormat]::Png)
$graphics.Dispose()
$bitmap.Dispose()
Write-Output "captured $width x $height at ($($rect.Left), $($rect.Top)) -> $Out"

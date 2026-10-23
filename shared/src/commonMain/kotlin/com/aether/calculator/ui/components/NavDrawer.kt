package com.aether.calculator.ui.components

/**
 * The navigation drawer that used to slide in from the left edge.
 *
 * It has been replaced by [OverflowMenu]: the kebab button now opens a menu that hangs
 * directly under the button instead of a panel that sweeps in from the side of the
 * screen. Every action the drawer offered is still available from the menu.
 */
@Deprecated(
    message = "The side drawer was replaced by the anchored overflow menu.",
    replaceWith = ReplaceWith("OverflowMenu(controller)"),
)
@Suppress("unused")
private object RetiredNavDrawer

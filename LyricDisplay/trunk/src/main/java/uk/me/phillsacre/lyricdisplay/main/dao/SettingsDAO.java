/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.dao;

import java.util.List;

import uk.me.phillsacre.lyricdisplay.main.entities.Setting;

/**
 * 
 * @author Phill
 * @since 29 Nov 2011
 */
public interface SettingsDAO
{
    List<Setting> getSettings();

    Setting getSetting(String name);

    void saveSetting(Setting setting);

    void saveSetting(String name, String value);
}

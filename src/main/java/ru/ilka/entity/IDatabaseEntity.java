package ru.ilka.entity;

import java.io.Serializable;

/**
 * Classes that do not implement this interface will not have any of their state serialized, deserialized or cloned.
 * @since %G%
 * @version %I%
 */
public interface IDatabaseEntity extends Serializable, Cloneable {
}

package com.kush.lib.persistence.helpers;

import com.kush.lib.persistence.api.Identifier;

public interface IdGenerator {

    Identifier next();
}

/*
 * Copyright 2018 Aurélien Gâteau <mail@agateau.com>
 *
 * This file is part of Pixel Wheels.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.agateau.ui;

/**
 * Responds to input from keyboard or gamepad
 */
public class UiInputMapper implements InputMapper {
    private KeyMapper mKeyMapper = KeyMapper.getDefaultInstance();
    private GamepadInputMapper mGamepadInputMapper = GamepadInputMapper.create(0);

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean isKeyPressed(VirtualKey key) {
        if (mKeyMapper.isKeyPressed(key)) {
            return true;
        }
        if (mGamepadInputMapper != null && mGamepadInputMapper.isKeyPressed(key)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean isKeyJustPressed(VirtualKey key) {
        if (mKeyMapper.isKeyJustPressed(key)) {
            return true;
        }
        if (mGamepadInputMapper != null && mGamepadInputMapper.isKeyJustPressed(key)) {
            return true;
        }
        return false;
    }
}

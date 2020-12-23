package org.palliums.libracore.common

/**
 * Created by elephant on 12/23/20 11:39 AM.
 * Copyright © 2019-2020. All rights reserved.
 * <p>
 * desc:
 */

/**
 * mnemonic related
 */
const val MNEMONIC_SALT_DEFAULT = "DIEM"
const val MNEMONIC_SALT_PREFIX = "DIEM WALLET: mnemonic salt prefix\$"

/**
 * key related
 */
const val MASTER_KEY_SALT = "DIEM WALLET: main key salt\$"
const val DERIVED_KEY_INFO_PREFIX = "DIEM WALLET: derived key\$"

/**
 * transaction related
 */
const val TRANSACTION_HASH_PREFIX = "DIEM::"
const val RAW_TRANSACTION_HASH_SALT = "${TRANSACTION_HASH_PREFIX}RawTransaction"

/**
 * currency related
 */
const val CURRENCY_DEFAULT_CODE = "XUS"
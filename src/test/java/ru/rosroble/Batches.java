package ru.rosroble;

public class Batches {
    public static final String noUpdateDateImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "price": 234,
                  "type": "OFFER"
                }
              ]
            }""";

    public static final String unexpectedFieldImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "owner: "zxc"
                  "price": 234,
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }""";

    public static final String negativePriceImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "price": -1,
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"

            }""";

    public static final String badPriceTypeImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "price": 123abc,
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }""";

    public static final String noPriceOfferImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }""";

    public static final String notNullPriceCategoryImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "price": 400,
                  "type": "CATEGORY"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }""";

    public static final String categoryPriceCheck = """
            {
                    "items": [
                        {
                            "type": "CATEGORY",
                            "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                            "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"
                        },
                        {
                            "type": "OFFER",
                            "name": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                            "id": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                            "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                            "price": 60
                        },
                        {
                            "type": "OFFER",
                            "name": "ffc7d977-e019-4e22-9424-178273b08a72",
                            "id": "ffc7d977-e019-4e22-9424-178273b08a72",
                            "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                            "price": 90
                        },
                        {
                            "type": "CATEGORY",
                            "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                            "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                            "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"
                        },
                        {
                            "type": "CATEGORY",
                            "name": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                            "id": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                            "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048"
                        },
                        {
                            "type": "CATEGORY",
                            "name": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                            "id": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                            "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048"
                        },
                        {
                            "type": "OFFER",
                            "name": "adec9f7d-dad6-408a-9952-f90268a16622",
                            "id": "adec9f7d-dad6-408a-9952-f90268a16622",
                            "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                            "price": 100
                        },
                        {
                            "type": "CATEGORY",
                            "name": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                            "id": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                            "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d"
                        },
                        {
                            "type": "OFFER",
                            "name": "18557df7-6617-419c-8008-6502f5e32a89",
                            "id": "18557df7-6617-419c-8008-6502f5e32a89",
                            "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                            "price": 80
                        },
                        {
                            "type": "OFFER",
                            "name": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                            "id": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                            "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                            "price": 60
                        }
                    ],
                    "updateDate": "2022-05-29T14:58:00Z"
                }""";

    public static final String[] categoryPriceCheckByParts = {"""
                {
                    "items": [
                        {
                            "type": "CATEGORY",
                            "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                            "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"
                        }],
                    "updateDate": "2022-05-29T14:58:00Z"
                }
                       """,
            """
               {
                    "items": [
                        {
                            "type": "OFFER",
                            "name": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                            "id": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                            "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                            "price": 60
                        },
                        {
                            "type": "OFFER",
                            "name": "ffc7d977-e019-4e22-9424-178273b08a72",
                            "id": "ffc7d977-e019-4e22-9424-178273b08a72",
                            "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                            "price": 90
                        },
                        {
                            "type": "CATEGORY",
                            "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                            "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                            "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"
                        }
                    ],
                    "updateDate": "2022-05-30T17:58:00Z"
               }
            """,
            """
            {
                    "items": [
                        {
                            "type": "CATEGORY",
                            "name": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                            "id": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                            "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048"
                        },
                        {
                            "type": "CATEGORY",
                            "name": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                            "id": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                            "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048"
                        },
                        {
                            "type": "OFFER",
                            "name": "adec9f7d-dad6-408a-9952-f90268a16622",
                            "id": "adec9f7d-dad6-408a-9952-f90268a16622",
                            "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                            "price": 100
                        }
                    ],
                    "updateDate": "2022-06-01T19:53:22.000Z"
               }
            """,
            """
            {
                    "items": [
                        {
                            "type": "CATEGORY",
                            "name": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                            "id": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                            "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d"
                        },
                        {
                            "type": "OFFER",
                            "name": "18557df7-6617-419c-8008-6502f5e32a89",
                            "id": "18557df7-6617-419c-8008-6502f5e32a89",
                            "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                            "price": 80
                        }
                    ],
                    "updateDate": "2022-06-03T20:12:00.000Z"
               }
            """,
            """
            {
                    "items": [
                        {
                            "type": "OFFER",
                            "name": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                            "id": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                            "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                            "price": 60
                        }
                    ],
                    "updateDate": "2022-06-05T00:00:02.000Z"
               }
            """
    };

    public static final String[] categoryPriceCheckByPartsNodes1C = {
            """
            {
                "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "date": "2022-05-29T14:58:00.000Z",
                "parentId": null,
                "type": "CATEGORY",
                "price": null,
                "children": []
            }
            """,
            """
            {
                "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "date": "2022-05-30T17:58:00.000Z",
                "parentId": null,
                "type": "CATEGORY",
                "price": 75,
                "children": [
                    {
                        "id": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "name": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "date": "2022-05-30T17:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 60,
                        "children": null
                    },
                    {
                        "id": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "name": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "date": "2022-05-30T17:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 90,
                        "children": null
                    },
                    {
                        "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "date": "2022-05-30T17:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "CATEGORY",
                        "price": null,
                        "children": []
                    }
                ]
            }
            """,
            """
            {
                "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "date": "2022-06-01T19:53:22.000Z",
                "parentId": null,
                "type": "CATEGORY",
                "price": 83,
                "children": [
                    {
                        "id": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "name": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "date": "2022-05-30T17:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 60,
                        "children": null
                    },
                    {
                        "id": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "name": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "date": "2022-05-30T17:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 90,
                        "children": null
                    },
                    {
                        "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "date": "2022-06-01T19:53:22.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "CATEGORY",
                        "price": 100,
                        "children": [
                            {
                                "id": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "name": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "date": "2022-06-01T19:53:22.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": 100,
                                "children": [
                                    {
                                        "id": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "name": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "date": "2022-06-01T19:53:22.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": OFFER,
                                        "price": 100,
                                        "children": null
                                    }
                                ]
                            },
                            {
                                "id": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "name": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "date": "2022-06-01T19:53:22.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": null,
                                "children": []
                            }
                        ]
                    }
                ]
            }
            """,
            """
            {
                "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "date": "2022-06-03T20:12:00.000Z",
                "parentId": null,
                "type": "CATEGORY",
                "price": 82,
                "children": [
                    {
                        "id": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "name": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "date": "2022-05-30T17:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 60,
                        "children": null
                    },
                    {
                        "id": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "name": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "date": "2022-05-30T17:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 90,
                        "children": null
                    },
                    {
                        "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "date": "2022-06-03T20:12:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "CATEGORY",
                        "price": 90,
                        "children": [
                            {
                                "id": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "name": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "date": "2022-06-03T20:12:00.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": 90,
                                "children": [
                                    {
                                        "id": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "name": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "date": "2022-06-01T19:53:22.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": OFFER,
                                        "price": 100,
                                        "children": null
                                    },
                                    {
                                        "id": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                        "name": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                        "date": "2022-06-03T20:12:00.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": CATEGORY,
                                        "price": 80,
                                        "children": [
                                            {
                                                "id": "18557df7-6617-419c-8008-6502f5e32a89",
                                                "name": "18557df7-6617-419c-8008-6502f5e32a89",
                                                "date": "2022-06-03T20:12:00.000Z",
                                                "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                                "type": OFFER,
                                                "price": 80,
                                                "children": null
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "id": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "name": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "date": "2022-06-01T19:53:22.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": null,
                                "children": []
                            }
                        ]
                    }
                ]
            }
            """,
            """
           {
                "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "date": "2022-06-05T00:00:02.000Z",
                "parentId": null,
                "type": "CATEGORY",
                "price": 78,
                "children": [
                    {
                        "id": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "name": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "date": "2022-05-30T17:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 60,
                        "children": null
                    },
                    {
                        "id": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "name": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "date": "2022-05-30T17:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 90,
                        "children": null
                    },
                    {
                        "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "date": "2022-06-05T00:00:02.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "CATEGORY",
                        "price": 80,
                        "children": [
                            {
                                "id": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "name": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "date": "2022-06-05T00:00:02.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": 80,
                                "children": [
                                    {
                                        "id": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "name": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "date": "2022-06-01T19:53:22.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": OFFER,
                                        "price": 100,
                                        "children": null
                                    },
                                    {
                                        "id": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                        "name": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                        "date": "2022-06-05T00:00:02.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": CATEGORY,
                                        "price": 70,
                                        "children": [
                                            {
                                                "id": "18557df7-6617-419c-8008-6502f5e32a89",
                                                "name": "18557df7-6617-419c-8008-6502f5e32a89",
                                                "date": "2022-06-03T20:12:00.000Z",
                                                "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                                "type": OFFER,
                                                "price": 80,
                                                "children": null
                                            },
                                            {
                                                "id": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                                                "name": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                                                "date": "2022-06-05T00:00:02.000Z",
                                                "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                                "type": OFFER,
                                                "price": 60,
                                                "children": null
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "id": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "name": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "date": "2022-06-01T19:53:22.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": null,
                                "children": []
                            }
                        ]
                    }
                ]
            }
            """
    };

    public static final String categoryPriceCheckNodes1C = """
            {
                "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "date": "2022-05-29T14:58:00.000Z",
                "parentId": null,
                "type": "CATEGORY",
                "price": 78,
                "children": [
                    {
                        "id": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "name": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 60,
                        "children": null
                    },
                    {
                        "id": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "name": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 90,
                        "children": null
                    },
                    {
                        "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "CATEGORY",
                        "price": 80,
                        "children": [
                            {
                                "id": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "name": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "date": "2022-05-29T14:58:00.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": null,
                                "children": []
                            },
                            {
                                "id": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "name": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "date": "2022-05-29T14:58:00.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": 80,
                                "children": [
                                    {
                                        "id": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "name": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "date": "2022-05-29T14:58:00.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": "OFFER",
                                        "price": 100,
                                        "children": null
                                    },
                                    {
                                        "id": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                        "name": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                        "date": "2022-05-29T14:58:00.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": "CATEGORY",
                                        "price": 70,
                                        "children": [
                                            {
                                                "id": "18557df7-6617-419c-8008-6502f5e32a89",
                                                "name": "18557df7-6617-419c-8008-6502f5e32a89",
                                                "date": "2022-05-29T14:58:00.000Z",
                                                "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                                "type": "OFFER",
                                                "price": 80,
                                                "children": null
                                            },
                                            {
                                                "id": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                                                "name": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                                                "date": "2022-05-29T14:58:00.000Z",
                                                "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                                "type": "OFFER",
                                                "price": 60,
                                                "children": null
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }""";

    public static final String nodesAfter8CDelete =
            """
            {
                "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "date": "2022-05-29T14:58:00.000Z",
                "parentId": null,
                "type": "CATEGORY",
                "price": 83,
                "children": [
                    {
                        "id": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "name": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 60,
                        "children": null
                    },
                    {
                        "id": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "name": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 90,
                        "children": null
                    },
                    {
                        "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "CATEGORY",
                        "price": 100,
                        "children": [
                            {
                                "id": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "name": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "date": "2022-05-29T14:58:00.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": null,
                                "children": []
                            },
                            {
                                "id": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "name": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "date": "2022-05-29T14:58:00.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": 100,
                                "children": [
                                    {
                                        "id": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "name": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "date": "2022-05-29T14:58:00.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": "OFFER",
                                        "price": 100,
                                        "children": null
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
            """;

    public static final String categoryPriceCheckChangeParent =
            """
            {
                    "items": [
                        {
                            "type": "CATEGORY",
                            "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                            "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                            "parentId": null
                        }
                    ],
                    "updateDate": "2022-05-29T14:58:00Z"
                }
            """;

    public static final String categoryPriceCheckChangeParentNodes1C =
            """
            {
                "id": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "name": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                "date": "2022-05-29T14:58:00.000Z",
                "parentId": null,
                "type": "CATEGORY",
                "price": 75,
                "children": [
                    {
                        "id": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "name": "ad5f443d-a5d5-4c20-b62c-f598b2999484",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 60,
                        "children": null
                    },
                    {
                        "id": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "name": "ffc7d977-e019-4e22-9424-178273b08a72",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2",
                        "type": "OFFER",
                        "price": 90,
                        "children": null
                    }
                ]
            }
            """;

    public static final String categoryPriceCheckChangeParentNodes4C =
            """
            {
                        "id": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "name": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": null,
                        "type": "CATEGORY",
                        "price": 80,
                        "children": [
                            {
                                "id": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "name": "de066eb3-f9e2-4cbf-9abc-a739864bb5fa",
                                "date": "2022-05-29T14:58:00.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": null,
                                "children": []
                            },
                            {
                                "id": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "name": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                "date": "2022-05-29T14:58:00.000Z",
                                "parentId": "07698dfe-cba2-4a9e-8f6d-50fa3bc12048",
                                "type": "CATEGORY",
                                "price": 80,
                                "children": [
                                    {
                                        "id": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "name": "adec9f7d-dad6-408a-9952-f90268a16622",
                                        "date": "2022-05-29T14:58:00.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": "OFFER",
                                        "price": 100,
                                        "children": null
                                    },
                                    {
                                        "id": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                        "name": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                        "date": "2022-05-29T14:58:00.000Z",
                                        "parentId": "afc5f4bf-2430-46d4-9437-c209783a8a1d",
                                        "type": "CATEGORY",
                                        "price": 70,
                                        "children": [
                                            {
                                                "id": "18557df7-6617-419c-8008-6502f5e32a89",
                                                "name": "18557df7-6617-419c-8008-6502f5e32a89",
                                                "date": "2022-05-29T14:58:00.000Z",
                                                "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                                "type": "OFFER",
                                                "price": 80,
                                                "children": null
                                            },
                                            {
                                                "id": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                                                "name": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                                                "date": "2022-05-29T14:58:00.000Z",
                                                "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                                                "type": "OFFER",
                                                "price": 60,
                                                "children": null
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
            """;

    public static final String importNoParentFoundBatch =
            """
                            {
                                "items": [
                                    {
                                        "id": "43ffda2b-ba09-4f63-be05-22b0544b7bc1",
                                        "name": "Оффер",
                                        "parentId": null,
                                        "price": 234,
                                        "type": "OFFER"
                                    },
                                    {
                                        "id": "1234",
                                        "name": "zxc",
                                        "parentId": "e86baf51-84d7-474d-8f0f-7bd284c1bb87",
                                        "price": 34,
                                        "type": "OFFER"
                                    }
                                ],
                                "updateDate": "2022-05-29T14:58:00.000Z"
                            }
                    """;
    public static final String importSelfParentBatch =
            """
                            {
                                "items": [
                                    {
                                        "id": "d84a91c7-a10c-4301-aa13-3c134cb5edbe",
                                        "name": "SelfParent",
                                        "parentId": "d84a91c7-a10c-4301-aa13-3c134cb5edbe",
                                        "price": 234,
                                        "type": "CATEGORY"
                                    }
                                ],
                                "updateDate": "2022-05-29T14:58:00.000Z"
                            }
                    """;

    public static final String offerAsParent =
                """
                {
                    "items": [
                        {
                            "id": "2681d79c-7d54-4782-b594-f9f3b8e4f181",
                            "name": "Оффер",
                            "parentId": null,
                            "price": 234,
                            "type": "OFFER"
                        },
                        {
                            "id": "3c1fa369-3b76-4d3d-8867-1a6708674915",
                            "name": "zxc",
                            "parentId": "2681d79c-7d54-4782-b594-f9f3b8e4f181",
                            "price": 34,
                            "type": "OFFER"
                        }
                    ],
                    "updateDate": "2022-05-29T14:58:00.000Z"
                }
                """;

    public static final String salesExpectedBatch =
            """
            {
                "items": [
                    {
                        "id": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                        "name": "f819abd3-4d45-46f7-a89d-ee911bd4e829",
                        "date": "2022-06-05T00:00:02.000Z",
                        "parentId": "38da9384-fc68-4ff4-ad9e-4620227bd232",
                        "price": 60,
                        "type": "OFFER"
                    }
                ]
            }
            """;

    public static final String badUUIDimport1 =
            """
            {
              "items": [
                {
                  "id": "123",
                  "name": "Оффер",
                  "price": 234,
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }
            """;

    public static final String badUUIDimport2 =
            """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f",
                  "name": "Оффер",
                  "price": 234,
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }
            """;

    public static final String badUUIDimport3 =
            """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963fabcdez",
                  "name": "Оффер",
                  "price": 234,
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }
            """;
}

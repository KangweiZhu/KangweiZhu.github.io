class Test {
    private static final Integer CUSTOM_RACKET_FITTING_EQUIP_FEE = 150;
    private static final Integer SHOE_FITTING_EQUIP_FEE = 100;
    private static final Integer GREEN_LEVEL_MEMBERSHIP_FEE = 50;
    private static final Integer PLATINUM_LEVEL_MEMBERSHIP_FEE = 200;
    private static final Integer BUNDLES_PRICE = 200;
    private static final Double PLATINUM_LEVEL_MEMBERSHIP_DISCOUNT_GLO = 0.1;
    private static final Double GREEN_LEVEL_DISCOUNT_IN_EQUIP = 0.1;
    private static final Double BUNDLE_DISCOUNT = 0.1;
    private static final Double SALES_TAX_RATE = 0.07;
    
    private static Double totalPricePreTax = 0.00;
    private static Double salesTax = 0.00;
    private static Double finalCost = 0.00;
    private static Double discount = 0.00;

    public static void main(String[] args) {
        calculator(false, true, 0, false, "null", 0);
        calculator(true, false, 3, true, "Green", 1);
        calculator(true, true, 5, true, "Platinum", 2);
    }

    public static void calculator(boolean isEquipmentRacketSelected, boolean isEquipmentShoesSelected, int numLessonBundles, boolean isClubMembership, String memberShipType, int testCaseNum) {
        totalPricePreTax = 0.00;
        salesTax = 0.00;
        finalCost = 0.00;
        discount = 0.00;
        if (isClubMembership) {
            if (memberShipType.equals("Green")) {
                totalPricePreTax += GREEN_LEVEL_MEMBERSHIP_FEE;
                if (isEquipmentRacketSelected) {
                    discount += CUSTOM_RACKET_FITTING_EQUIP_FEE * GREEN_LEVEL_DISCOUNT_IN_EQUIP;
                    totalPricePreTax += CUSTOM_RACKET_FITTING_EQUIP_FEE;
                }
                if (isEquipmentShoesSelected) {
                    discount += SHOE_FITTING_EQUIP_FEE * GREEN_LEVEL_DISCOUNT_IN_EQUIP;
                    totalPricePreTax += SHOE_FITTING_EQUIP_FEE * GREEN_LEVEL_DISCOUNT_IN_EQUIP;
                }
                if (numLessonBundles >= 5) {
                    discount += BUNDLES_PRICE * numLessonBundles * BUNDLE_DISCOUNT;
                    totalPricePreTax += BUNDLES_PRICE * numLessonBundles;
                } else {
                    totalPricePreTax += BUNDLES_PRICE * numLessonBundles;
                } 
            } else {
                totalPricePreTax += PLATINUM_LEVEL_MEMBERSHIP_FEE; 
                if (isEquipmentRacketSelected) {
                    totalPricePreTax += CUSTOM_RACKET_FITTING_EQUIP_FEE;
                } 
                if (isEquipmentShoesSelected) {
                    totalPricePreTax += SHOE_FITTING_EQUIP_FEE;
                }
                if (numLessonBundles >= 5) {
                    discount += BUNDLES_PRICE * numLessonBundles * BUNDLE_DISCOUNT;
                    totalPricePreTax += BUNDLES_PRICE * numLessonBundles;
                } else {
                    totalPricePreTax += BUNDLES_PRICE * numLessonBundles;
                }
            }
        } else {
            if (isEquipmentRacketSelected) {
                totalPricePreTax += CUSTOM_RACKET_FITTING_EQUIP_FEE;
            } 
            if (isEquipmentShoesSelected) {
                totalPricePreTax += SHOE_FITTING_EQUIP_FEE;
            }
            if (numLessonBundles >= 5) {
                discount += BUNDLES_PRICE * numLessonBundles * BUNDLE_DISCOUNT;
                totalPricePreTax += BUNDLES_PRICE * numLessonBundles;
            } else {
                totalPricePreTax += BUNDLES_PRICE * numLessonBundles;
            } 
        }
        if (memberShipType.equals("Platinum")) {
            discount += totalPricePreTax * PLATINUM_LEVEL_MEMBERSHIP_DISCOUNT_GLO;
        }
        salesTax = (totalPricePreTax - discount) * SALES_TAX_RATE;
        finalCost = salesTax + totalPricePreTax - discount;
        System.out.printf("------------ Test Case %d ------------\n", testCaseNum);
        System.out.printf("Total Price = %.2f\n", totalPricePreTax);
        System.out.printf("Discount = %.2f\n", discount);
        System.out.printf("Sales Tax = %.2f\n", salesTax);
        System.out.printf("Final Cost = %.2f\n", finalCost);
    }
}
export interface EcoReport {
  id?: number;
  detectedLanguage: string;
  loopsFound: number;
  memoryAllocations: number;
  carbonEmissions: string;
  electricityCost: string;
  suggestion: string;
}
